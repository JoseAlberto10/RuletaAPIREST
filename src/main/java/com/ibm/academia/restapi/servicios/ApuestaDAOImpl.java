package com.ibm.academia.restapi.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.academia.restapi.modelos.entidades.Apuesta;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;
import com.ibm.academia.restapi.repositorios.ApuestaRepository;

@Service
public class ApuestaDAOImpl extends GenericoDAOImpl<Apuesta, ApuestaRepository> implements ApuestaDAO
{
	
	@Autowired
	public ApuestaDAOImpl(ApuestaRepository repository) 
	{
		super(repository);
	}

	@Override
	public void asignarRuleta(Apuesta apuesta, Ruleta ruleta) 
	{
		apuesta.setRuleta(ruleta);
		repository.save(apuesta);
	}
}
