package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Persona;
import com.desi.entidades.Propiedad;
import com.desi.presentacion.ContratoForm;

public interface ContratoService {

	void registrar(ContratoForm form);

	List<Propiedad> obtenerPropiedadesDisponibles();

	List<Persona> obtenerInquilinos();
}