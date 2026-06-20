package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Propiedad;
import com.desi.presentacion.PublicacionForm;

public interface PublicacionService {

	void registrar(PublicacionForm form);

	List<Propiedad> obtenerPropiedadesDisponibles();
}
