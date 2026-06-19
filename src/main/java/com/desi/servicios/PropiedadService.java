package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Ciudad;
import com.desi.entidades.Persona;
import com.desi.entidades.Propiedad;
import com.desi.presentacion.PropiedadForm;

public interface PropiedadService {

	void registrar(PropiedadForm form);

	void eliminar(Long id);

	List<Propiedad> obtenerActivas();

	List<Propiedad> obtenerTodasActivas();

	List<Persona> obtenerPropietarios();

	List<Ciudad> obtenerCiudades();
}
