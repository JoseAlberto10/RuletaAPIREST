package com.ibm.academia.restapi.controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ibm.academia.restapi.excepciones.BadRequestException;
import com.ibm.academia.restapi.excepciones.NotFoundException;
import com.ibm.academia.restapi.mapper.RuletaMapper;
import com.ibm.academia.restapi.modelos.dto.RuletaDTO;
import com.ibm.academia.restapi.modelos.entidades.Apuesta;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;
import com.ibm.academia.restapi.servicios.ApuestaDAO;
import com.ibm.academia.restapi.servicios.RuletaDAO;

@RestController
@RequestMapping("/ruleta") 
public class RuletaControlador
{
	@Autowired
	private RuletaDAO ruletaDao;
	
	@Autowired
	private ApuestaDAO apuestaDao;
	
	/**
	 * Endpoint para crear nueva ruleta 
	 * @return   Retorna un objeto de tipo Ruleta
	 * @author JARP 15/05/2022
	 */
    @PostMapping()
    public ResponseEntity<?> crearNuevaRuleta()
    {
        Ruleta ruleta = new Ruleta();
        ruleta.setEstaAbierta(Boolean.FALSE);
        ruletaDao.guardarRuleta(ruleta);
        return new ResponseEntity<Ruleta>(ruleta, HttpStatus.CREATED);
    }
    
    /**
     * Endpoint para aperturar una ruleta dado su id
     * @param ruletaId  Es el parametro que se pasa como id de la ruleta
     * @return          Retorna un objeto Map indicando el estado de la ruleta y su id
     * @author JARP 15/05/2022
     */
    @PutMapping("/aperturar/id/{ruletaId}")
    public ResponseEntity<?> aperturarRuleta(@PathVariable Long ruletaId)
    {
    	Map<Boolean, Object> respuesta = new HashMap<Boolean, Object>();	
        Optional<Ruleta> opRuleta = ruletaDao.buscarPorId(ruletaId);
        
        if (!opRuleta.isPresent())
            throw new NotFoundException(String.format("La ruleta con Id: %d no existe",ruletaId));
        
        if (opRuleta.get().getEstaAbierta() == Boolean.TRUE)
            throw new BadRequestException(String.format("La ruleta con Id: %d ya se encuentra abierta",ruletaId));
        
        respuesta.put(ruletaDao.abrirRuleta(opRuleta.get()), "Se ha abierto la ruleta ID: " + ruletaId);
        return new ResponseEntity<Map<Boolean, Object>>(respuesta,HttpStatus.ACCEPTED);
    }
    
    
    /**
     * Endpoint para realizar una apuesta ya sea por numero o color
     * @param ruletaId Es el id de la ruleta que contendra a la apuesta que se realice
     * @param apuesta  Es el objeto JSON con la informacion de la apuesta para generarla
     * @param result   Seguido del objeto se indica que va a validar
     * @return         Retorna un objeto de tipo Apuesta, que es la apuesta realizada
     * @author JARP 15/05/2022
     */
    @PutMapping("/apostar/id/{ruletaId}")
    public ResponseEntity<?> apostarRuleta(@PathVariable Long ruletaId, @Valid @RequestBody Apuesta apuesta, BindingResult result)
    {
		Map<String, Object> validaciones = new HashMap<String, Object>();
		if(result.hasErrors())
		{
			List<String> listaErrores = result.getFieldErrors()
					.stream()
					.map(errores -> "Campo: " + errores.getField() + " " + "Causa del error: " +errores.getDefaultMessage())
					.collect(Collectors.toList());
			validaciones.put("Lista Errores", listaErrores);
			return new ResponseEntity<Map<String, Object>>(validaciones, HttpStatus.BAD_REQUEST);
		}

		
        Optional<Ruleta> opRuleta = ruletaDao.buscarPorId(ruletaId);
        
        if(!opRuleta.isPresent())
            throw new NotFoundException(String.format("La ruleta con Id: %d no existe",ruletaId));

        if(opRuleta.get().getEstaAbierta() == Boolean.FALSE)
            throw new BadRequestException(String.format("La ruleta con Id: %d se encuentra cerrada",ruletaId));
        
        if(apuesta.getApuestaColor() !=null) 
        {
            if(apuesta.getApuestaNumero() != null)
            {
                throw new NotFoundException(String.format("Apuesta a un numero o color"));
            }
        }
        else if (apuesta.getApuestaNumero() == null)
        {
            throw new NotFoundException(String.format("Debe realizar una apuesta, ya sea a numero o color"));
        }  
        
        Apuesta apuestaRealizada = apuestaDao.guardar(apuesta);
        apuestaDao.asignarRuleta(apuestaRealizada, opRuleta.get());
        return new ResponseEntity<Apuesta>(apuestaRealizada, HttpStatus.OK);
    }
    
    
    /**
     * Endpoint para cerrar una ruleta dado su id
     * @param ruletaId   Es el id de la ruleta que se va a cerrar
     * @return           Retorna la lista de apuestas hechas
     * @author JARP 15/05/2022
     */
    @PutMapping("/cerrar/id/{ruletaId}")
    public ResponseEntity<?> cerrarRuleta(@PathVariable Long ruletaId)
    {
        Optional<Ruleta> opRuleta = ruletaDao.buscarPorId(ruletaId);
        
        if(!opRuleta.isPresent())
            throw new NotFoundException(String.format("La ruleta con Id: %d no existe",ruletaId));

        if(opRuleta.get().getEstaAbierta() == Boolean.FALSE)
            throw new BadRequestException(String.format("La ruleta con Id: %d se encuentra cerrada",ruletaId));
        
        opRuleta.get().setEstaAbierta(Boolean.FALSE);
        ruletaDao.guardarRuleta(opRuleta.get());
        
        List<Apuesta> apuestas = opRuleta.get().getApuestas();
        if(apuestas.isEmpty())
            throw new NotFoundException(String.format("No se han hecho apuestas en esta ruleta"));
        
        return new ResponseEntity<List<Apuesta>>(apuestas,HttpStatus.OK);
    }
    
    /**
     * Endpoint para retornar lista con todas las ruletas
     * BadRequestException En caso de que falle ya que no existen ruletas
     * @return  		   Retorna la lista de ruletas si existen
     * @author JARP 15/05/2022
     */
	@GetMapping("/lista/ruletas")
	public List<Ruleta> buscarTodas()    
	{ 
		List<Ruleta> ruletas = (List<Ruleta>) ruletaDao.mostrarTodos();
		
		if(ruletas.isEmpty())
			throw new NotFoundException("No existen ruletas");       
		return ruletas;
	}
	
	
	/**
	 * Endpoint para consultar todas las ruletas, con solo id, estado y fechaAlta
	 * @return  Retorna una lista de ruletas en DTO
	 * @NotFoundException En caso de que no encuentre ruletas en la BDD 	
	 * @author JARP 16/05/2022
	 */
	@GetMapping("/ruletas/dto")
	public ResponseEntity<?> buscarRuletasDTO()   
	{ 
		List<Ruleta> ruletas = (List<Ruleta>) ruletaDao.mostrarTodos();
		
		if(ruletas.isEmpty())                       
			throw new NotFoundException(String.format("No existen ruletas en la base de datos"));
		
		List<RuletaDTO> listaRuletas = ruletas
				.stream()
				.map(RuletaMapper::mapRuleta)
				.collect(Collectors.toList());
		return new ResponseEntity<List<RuletaDTO>>(listaRuletas, HttpStatus.OK);
	}
		
}
