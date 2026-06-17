package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Ciudad;
import com.desi.entidades.Persona;
import com.desi.presentacion.PropiedadForm;

public interface PropiedadService {

	void registrar(PropiedadForm form);

	List<Persona> obtenerPropietarios();

	List<Ciudad> obtenerCiudades();
}
