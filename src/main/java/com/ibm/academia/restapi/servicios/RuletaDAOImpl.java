package com.ibm.academia.restapi.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.academia.restapi.modelos.entidades.Apuesta;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;
import com.ibm.academia.restapi.repositorios.RuletaRepository;

@Service
public class RuletaDAOImpl extends GenericoDAOImpl<Ruleta, RuletaRepository> implements RuletaDAO
{

	@Autowired
	public RuletaDAOImpl(RuletaRepository repository)
	{
		super(repository);
	}

	
	@Override
	public Boolean abrirRuleta(Ruleta ruleta)
	{
		ruleta.setEstaAbierta(Boolean.TRUE);
		repository.save(ruleta);
		return Boolean.TRUE;
	}

	@Override
	public Long guardarRuleta(Ruleta ruleta)
	{
		Long ruletaId = repository.save(ruleta).getId();
		return ruletaId;
	}

	@Override
	public List<Apuesta> cerrarRuleta(Ruleta ruleta) 
	{
        ruleta.setEstaAbierta(Boolean.FALSE);
        List<Apuesta> apuestasRealizadas = ruleta.getApuestas();
        apuestasRealizadas.clear();
        ruleta.setApuestas(apuestasRealizadas);
        repository.save(ruleta);
        return apuestasRealizadas;
	}

}
