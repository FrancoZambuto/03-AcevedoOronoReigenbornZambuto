package com.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.desi.entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.accesoDatos.ContratoRepository;
import com.desi.accesoDatos.PersonaRepository;
import com.desi.accesoDatos.PropiedadRepository;
import com.desi.presentacion.ContratoForm;

@Service
public class ContratoServiceImpl implements ContratoService {

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Override
	@Transactional
	public void registrar(ContratoForm form) {
		validarCampos(form);

		EstadoContrato estado = form.getEstado();
		if (estado == null) {
			estado = EstadoContrato.BORRADOR;
		}

		Propiedad propiedad = propiedadRepository.findByIdAndEliminadaFalse(form.getPropiedadId())
				.orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada o eliminada"));

		Persona inquilino = personaRepository.findByIdAndEliminadaFalse(form.getInquilinoId())
				.orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado o eliminado"));

		if (estado == EstadoContrato.ACTIVO) {
			validarActivacionContrato(propiedad);
		}

		Contrato contrato = new Contrato();
		contrato.setPropiedad(propiedad);
		contrato.setInquilino(inquilino);
		contrato.setFechaInicio(form.getFechaInicio());
		contrato.setDuracionMeses(form.getDuracionMeses());
		contrato.setImporteMensual(form.getImporteMensual());
		contrato.setDiaVencimientoMensual(form.getDiaVencimientoMensual());
		contrato.setDescripcion(form.getDescripcion());
		contrato.setEstado(estado);
		contrato.setEliminado(false);

		HistorialEstadoContrato historial = new HistorialEstadoContrato();
		historial.setEstado(estado);
		historial.setFechaHora(LocalDateTime.now());
		historial.setContrato(contrato);

		contrato.getHistorialEstados().add(historial);

		if (estado == EstadoContrato.ACTIVO) {
			propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);

			HistorialEstadoPropiedad historialEstadoPropiedad = new HistorialEstadoPropiedad(
					null,
					EstadoDisponibilidad.ALQUILADA,
					LocalDateTime.now(),
					propiedad);
			propiedad.getHistorialEstados().add(historialEstadoPropiedad);
		}
		contratoRepository.save(contrato);
	}

	@Override
	public List<Propiedad> obtenerPropiedadesDisponibles() {
		return propiedadRepository.findByEliminadaFalseAndEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
	}

	@Override
	public List<Persona> obtenerInquilinos() {
		return personaRepository.findByEliminadaFalse();
	}

	private void validarActivacionContrato(Propiedad propiedad) {
		if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
			throw new IllegalArgumentException("No se puede crear un contrato activo si la propiedad no está disponible");
		}

		boolean existeContratoActivo = contratoRepository.existsByPropiedadAndEstadoAndEliminadoFalse(
				propiedad,
				EstadoContrato.ACTIVO
		);

		if (existeContratoActivo) {
			throw new IllegalArgumentException("La propiedad ya tiene un contrato activo");
		}
	}

	private void validarCampos(ContratoForm form) {
		if (form.getPropiedadId() == null) {
			throw new IllegalArgumentException("La propiedad es obligatoria");
		}
		if (form.getInquilinoId() == null) {
			throw new IllegalArgumentException("El inquilino es obligatorio");
		}
		if (form.getFechaInicio() == null) {
			throw new IllegalArgumentException("La fecha de inicio es obligatoria");
		}
		if (form.getDuracionMeses() == null || form.getDuracionMeses() <= 0) {
			throw new IllegalArgumentException("La duración en meses debe ser un número positivo");
		}
		if (form.getImporteMensual() == null || form.getImporteMensual().compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("El importe mensual debe ser un número positivo");
		}
		if (form.getDiaVencimientoMensual() == null
				|| form.getDiaVencimientoMensual() < 1
				|| form.getDiaVencimientoMensual() > 31) {
			throw new IllegalArgumentException("El día de vencimiento mensual debe estar entre 1 y 31");
		}
		if (form.getDescripcion() == null || form.getDescripcion().isBlank()) {
			throw new IllegalArgumentException("La descripción es obligatoria");
		}
	}
}