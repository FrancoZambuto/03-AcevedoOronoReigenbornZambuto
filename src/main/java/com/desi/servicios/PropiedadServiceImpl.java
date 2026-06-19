package com.desi.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.accesoDatos.CiudadRepository;
import com.desi.accesoDatos.ContratoRepository;
import com.desi.accesoDatos.PersonaRepository;
import com.desi.accesoDatos.PropiedadRepository;
import com.desi.entidades.Ciudad;
import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.HistorialEstadoPropiedad;
import com.desi.entidades.Persona;
import com.desi.entidades.Propiedad;
import com.desi.excepciones.PropiedadConContratoActivoException;
import com.desi.excepciones.PropiedadDuplicadaException;
import com.desi.presentacion.PropiedadForm;

@Service
public class PropiedadServiceImpl implements PropiedadService {

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Override
	@Transactional
	public void registrar(PropiedadForm form) {
		validarCampos(form);

		EstadoDisponibilidad estado = form.getEstadoDisponibilidad();
		if (estado == null) {
			estado = EstadoDisponibilidad.DISPONIBLE;
		}

		Ciudad ciudad = ciudadRepository.findById(form.getCiudadId())
				.orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada"));

		if (propiedadRepository.existeActivaPorDireccionYCiudad(form.getDireccion(), ciudad)) {
			throw new PropiedadDuplicadaException("Ya existe una propiedad activa con la misma direccion y ciudad");
		}

		Persona propietario = personaRepository.buscarActivaPorId(form.getPropietarioId())
				.orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado o no disponible"));

		Propiedad propiedad = new Propiedad();
		propiedad.setDireccion(form.getDireccion());
		propiedad.setTipo(form.getTipo());
		propiedad.setCantidadAmbientes(form.getCantidadAmbientes());
		propiedad.setMetrosCuadrados(form.getMetrosCuadrados());
		propiedad.setDescripcion(form.getDescripcion());
		propiedad.setComodidades(form.getComodidades());
		propiedad.setEstadoDisponibilidad(estado);
		propiedad.setEliminada(false);
		propiedad.setCiudad(ciudad);
		propiedad.setPropietario(propietario);

		HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
		historial.setEstado(estado);
		historial.setFechaHora(LocalDateTime.now());
		historial.setPropiedad(propiedad);
		propiedad.getHistorialEstados().add(historial);

		propiedadRepository.save(propiedad);
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Propiedad propiedad = propiedadRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));

		if (propiedad.isEliminada()) {
			throw new IllegalArgumentException("La propiedad ya se encuentra eliminada");
		}

		if (contratoRepository.tieneContratoActivo(id)) {
			throw new PropiedadConContratoActivoException(
					"No se puede eliminar la propiedad porque tiene un contrato activo vigente");
		}

		propiedad.setEliminada(true);
		propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.INACTIVA);

		HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
		historial.setEstado(EstadoDisponibilidad.INACTIVA);
		historial.setFechaHora(LocalDateTime.now());
		historial.setPropiedad(propiedad);
		propiedad.getHistorialEstados().add(historial);

		propiedadRepository.save(propiedad);
	}

	@Override
	public List<Propiedad> obtenerActivas() {
		return propiedadRepository.listarActivas();
	}

	@Override
	public List<Propiedad> obtenerTodasActivas() {
		return propiedadRepository.listarActivas();
	}

	@Override
	public List<Persona> obtenerPropietarios() {
		return personaRepository.listarActivas();
	}

	@Override
	public List<Ciudad> obtenerCiudades() {
		return ciudadRepository.findAll();
	}

	private void validarCampos(PropiedadForm form) {
		if (form.getDireccion() == null || form.getDireccion().isBlank()) {
			throw new IllegalArgumentException("La direccion es obligatoria");
		}
		if (form.getTipo() == null) {
			throw new IllegalArgumentException("El tipo de propiedad es obligatorio");
		}
		if (form.getCantidadAmbientes() == null || form.getCantidadAmbientes() <= 0) {
			throw new IllegalArgumentException("La cantidad de ambientes debe ser un entero positivo");
		}
		if (form.getMetrosCuadrados() == null || form.getMetrosCuadrados() <= 0) {
			throw new IllegalArgumentException("Los metros cuadrados deben ser un numero positivo");
		}
		if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
			throw new IllegalArgumentException("La descripcion es obligatoria");
		}
		if (form.getComodidades() == null || form.getComodidades().isBlank()) {
			throw new IllegalArgumentException("Las comodidades son obligatorias");
		}
		if (form.getCiudadId() == null) {
			throw new IllegalArgumentException("La ciudad es obligatoria");
		}
		if (form.getPropietarioId() == null) {
			throw new IllegalArgumentException("El propietario es obligatorio");
		}
	}
}
