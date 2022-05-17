package com.ibm.academia.restapi.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.academia.restapi.modelos.entidades.Apuesta;

@Repository
public interface ApuestaRepository extends CrudRepository<Apuesta, Integer>
{

}
