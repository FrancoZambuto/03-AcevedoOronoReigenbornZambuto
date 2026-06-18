package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Contrato;
import com.desi.entidades.Persona;
import com.desi.entidades.Propiedad;
import com.desi.presentacion.ContratoForm;

public interface ContratoService {

	void registrar(ContratoForm form);

	void eliminar(Long id);

	List<Propiedad> obtenerPropiedadesDisponibles();

	List<Persona> obtenerInquilinos();

	List<Contrato> obtenerActivos();
}