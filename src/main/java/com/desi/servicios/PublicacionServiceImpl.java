package com.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.accesoDatos.CiudadRepository;
import com.desi.accesoDatos.PropiedadRepository;
import com.desi.accesoDatos.PublicacionRepository;
import com.desi.entidades.Ciudad;
import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.EstadoPublicacion;
import com.desi.entidades.HistorialEstadoPublicacion;
import com.desi.entidades.Propiedad;
import com.desi.entidades.Publicacion;
import com.desi.excepciones.PublicacionActivaExistenteException;
import com.desi.excepciones.PublicacionNoEliminableException;
import com.desi.presentacion.PublicacionFiltroForm;
import com.desi.presentacion.PublicacionForm;

@Service
public class PublicacionServiceImpl implements PublicacionService {

	@Autowired
	private PublicacionRepository publicacionRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private CiudadRepository ciudadRepository;

	@Override
	@Transactional
	public void registrar(PublicacionForm form) {
		validarCampos(form);

		Propiedad propiedad = propiedadRepository.buscarPorIdNoEliminada(form.getPropiedadId())
				.orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada o eliminada"));

		if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
			throw new IllegalArgumentException("Solo se puede publicar una propiedad que se encuentre disponible");
		}

		EstadoPublicacion estado = form.getEstado();
		if (estado == null) {
			estado = EstadoPublicacion.ACTIVA;
		}

		if (estado == EstadoPublicacion.ACTIVA
				&& publicacionRepository.existePublicacionConEstadoParaPropiedad(form.getPropiedadId(), EstadoPublicacion.ACTIVA)) {
			throw new PublicacionActivaExistenteException("Ya existe una publicacion activa para esta propiedad");
		}

		Publicacion publicacion = new Publicacion();
		publicacion.setPropiedad(propiedad);
		publicacion.setPrecioMensual(form.getPrecioMensual());
		publicacion.setCondiciones(form.getCondiciones());
		publicacion.setDescripcion(form.getDescripcion());
		publicacion.setFechaPublicacion(form.getFechaPublicacion());
		publicacion.setEstado(estado);
		publicacion.setEliminada(false);

		HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
		historial.setEstado(estado);
		historial.setFechaHora(LocalDateTime.now());
		historial.setPublicacion(publicacion);
		publicacion.getHistorialEstados().add(historial);

		publicacionRepository.save(publicacion);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Publicacion publicacion = publicacionRepository.buscarPorIdNoEliminada(id)
				.orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));

		if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
			throw new PublicacionNoEliminableException(
					"Solo se pueden eliminar publicaciones en estado ACTIVA");
		}

		publicacion.setEliminada(true);
		publicacionRepository.save(publicacion);
	}

	@Override
	public List<Publicacion> listarNoEliminadas() {
		return publicacionRepository.listarNoEliminadas();
	}

	@Override
	public PublicacionForm buscarParaEdicion(Long id) {
		Publicacion publicacion = publicacionRepository.buscarPorIdNoEliminada(id)
				.orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));

		PublicacionForm form = new PublicacionForm();
		form.setPropiedadId(publicacion.getPropiedad().getId());
		form.setPrecioMensual(publicacion.getPrecioMensual());
		form.setCondiciones(publicacion.getCondiciones());
		form.setDescripcion(publicacion.getDescripcion());
		form.setFechaPublicacion(publicacion.getFechaPublicacion());
		form.setEstado(publicacion.getEstado());
		return form;
	}

	@Override
	@Transactional
	public void actualizar(Long id, PublicacionForm form) {
		validarCamposEdicion(form);

		Publicacion publicacion = publicacionRepository.buscarPorIdNoEliminada(id)
				.orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));

		if (publicacion.getEstado() == EstadoPublicacion.FINALIZADA
				&& !publicacion.getCondiciones().equals(form.getCondiciones())) {
			throw new IllegalArgumentException(
					"No se pueden modificar las condiciones de una publicacion finalizada");
		}

		EstadoPublicacion nuevoEstado = form.getEstado();
		if (nuevoEstado == null) {
			nuevoEstado = publicacion.getEstado();
		}

		if (nuevoEstado == EstadoPublicacion.ACTIVA && publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
			Propiedad propiedad = publicacion.getPropiedad();
			if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
				throw new IllegalArgumentException(
						"Solo se puede activar una publicacion si la propiedad se encuentra disponible");
			}
			if (publicacionRepository.existePublicacionConEstadoParaPropiedad(propiedad.getId(), EstadoPublicacion.ACTIVA)) {
				throw new PublicacionActivaExistenteException(
						"Ya existe una publicacion activa para esta propiedad");
			}
		}

		EstadoPublicacion estadoAnterior = publicacion.getEstado();

		publicacion.setPrecioMensual(form.getPrecioMensual());
		publicacion.setCondiciones(form.getCondiciones());
		publicacion.setDescripcion(form.getDescripcion());
		publicacion.setFechaPublicacion(form.getFechaPublicacion());
		publicacion.setEstado(nuevoEstado);

		if (!nuevoEstado.equals(estadoAnterior)) {
			HistorialEstadoPublicacion historial = new HistorialEstadoPublicacion();
			historial.setEstado(nuevoEstado);
			historial.setFechaHora(LocalDateTime.now());
			historial.setPublicacion(publicacion);
			publicacion.getHistorialEstados().add(historial);
		}

		publicacionRepository.save(publicacion);
	}

	@Override
	public Publicacion buscarPorId(Long id) {
		return publicacionRepository.buscarPorIdNoEliminada(id)
				.orElseThrow(() -> new IllegalArgumentException("Publicacion no encontrada"));
	}

	@Override
	public List<Publicacion> filtrar(PublicacionFiltroForm filtro) {
		return publicacionRepository.filtrar(
				filtro.getPropiedadId(),
				filtro.getCiudadId(),
				filtro.getEstado(),
				filtro.getPrecioMin(),
				filtro.getPrecioMax());
	}

	@Override
	public List<Propiedad> obtenerPropiedadesDisponibles() {
		return propiedadRepository.listarPorEstadoDisponibilidadNoEliminadas(EstadoDisponibilidad.DISPONIBLE);
	}

	@Override
	public List<Propiedad> obtenerPropiedades() {
		return propiedadRepository.listarActivas();
	}

	@Override
	public List<Ciudad> obtenerCiudades() {
		return ciudadRepository.findAll();
	}

	private void validarCamposEdicion(PublicacionForm form) {
		if (form.getPrecioMensual() == null || form.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El precio mensual debe ser un numero positivo");
		}
		if (form.getCondiciones() == null || form.getCondiciones().isBlank()) {
			throw new IllegalArgumentException("Las condiciones son obligatorias");
		}
		if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
			throw new IllegalArgumentException("La descripcion es obligatoria");
		}
		if (form.getFechaPublicacion() == null) {
			throw new IllegalArgumentException("La fecha de publicacion es obligatoria");
		}
	}

	private void validarCampos(PublicacionForm form) {
		if (form.getPropiedadId() == null) {
			throw new IllegalArgumentException("La propiedad es obligatoria");
		}
		if (form.getPrecioMensual() == null || form.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El precio mensual debe ser un numero positivo");
		}
		if (form.getCondiciones() == null || form.getCondiciones().isBlank()) {
			throw new IllegalArgumentException("Las condiciones son obligatorias");
		}
		if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
			throw new IllegalArgumentException("La descripcion es obligatoria");
		}
		if (form.getFechaPublicacion() == null) {
			throw new IllegalArgumentException("La fecha de publicacion es obligatoria");
		}
	}
}
