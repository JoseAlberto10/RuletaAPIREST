package com.ibm.academia.restapi.servicios;

import java.util.Optional;

public interface GenericoDAO<Entidad>
{
    public Optional<Entidad> buscarPorId(Integer id);
    public Entidad guardar(Entidad entidad);
    public Iterable<Entidad> mostrarTodos();
    public void eliminar(Integer id);
}
