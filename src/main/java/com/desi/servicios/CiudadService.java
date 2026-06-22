package com.desi.servicios;


import com.desi.entidades.Ciudad;
import com.desi.excepciones.Excepcion;
import com.desi.presentacion.ciudades.CiudadesBuscarForm;

import java.util.List;

/**
 * Clase que permite gestionar la entidad Ciudad en el sistema.
 * @author kuttel
 * @version 1.0
 */

public interface CiudadService {



    /**
     * Obtiene la lista completa de ciudades
     * @return Todas las ciudades
     */
    List<Ciudad> getAll();

    /**
     * Obtiene una ciudad determinada
     * @param idCiudad Identificador de la ciudad buscada
     * @return Ciudad encontrada
     */
    Ciudad getById(Long idCiudad) ;

    List<Ciudad> filter(CiudadesBuscarForm filter) throws Excepcion;

    void deleteByid(Long id);

    void save(Ciudad c) throws Excepcion;

}
