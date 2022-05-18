package com.ibm.academia.restapi.servicios;

import java.util.List;

import com.ibm.academia.restapi.modelos.entidades.Apuesta;
import com.ibm.academia.restapi.modelos.entidades.Ruleta;

public interface RuletaDAO extends GenericoDAO<Ruleta>
{
    public Boolean abrirRuleta(Ruleta ruleta);
    public Long guardarRuleta(Ruleta ruleta);
    public List<Apuesta> cerrarRuleta(Ruleta ruleta);
}
