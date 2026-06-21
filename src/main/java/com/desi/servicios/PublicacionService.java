package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Propiedad;
import com.desi.entidades.Publicacion;
import com.desi.presentacion.PublicacionForm;

public interface PublicacionService {

	void registrar(PublicacionForm form);

	void eliminar(Long id);

	PublicacionForm buscarParaEdicion(Long id);

	void actualizar(Long id, PublicacionForm form);

	Publicacion buscarPorId(Long id);

	List<Publicacion> listarNoEliminadas();

	List<Propiedad> obtenerPropiedadesDisponibles();
}
