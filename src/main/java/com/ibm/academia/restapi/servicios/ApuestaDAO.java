package com.ibm.academia.restapi.servicios;

import com.ibm.academia.restapi.modelos.entidades.Apuesta;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;

public interface ApuestaDAO extends GenericoDAO<Apuesta>
{
	public void asignarRuleta(Apuesta apuesta, Ruleta ruleta);
}
