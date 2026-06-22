package com.desi.servicios;

import com.desi.entidades.Persona;
import com.desi.excepciones.Excepcion;
import com.desi.presentacion.personas.PersonasBuscarForm;
import java.util.List;

public interface PersonaService {



    List<Persona> getAll();

    List<Persona> filter(PersonasBuscarForm filter);

    /**
     * Si la persona existe la actualizará, sino la creará en BD
     * @param persona
     */
    void save(Persona persona) throws Excepcion;

    /**
     * permite obtener una persona determinada
     * @param idPersona identificador de la persona buscada
     * @return persona encontrada o null si no encontr{o la persona
     * @throws Exception ante un error
     */
    Persona getPersonaById(Long idPersona);

    void deletePersonaByid(Long id);


}
