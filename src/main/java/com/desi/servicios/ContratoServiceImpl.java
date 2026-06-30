package com.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
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

		Propiedad propiedad = propiedadRepository.buscarPorIdNoEliminada(form.getPropiedadId())
				.orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada o eliminada"));

		Persona inquilino = personaRepository.buscarActivaPorId(form.getInquilinoId())
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
		return propiedadRepository.listarPorEstadoDisponibilidadNoEliminadas(EstadoDisponibilidad.DISPONIBLE);
	}

	@Override
	public List<Persona> obtenerInquilinos() {
		return personaRepository.listarActivas();
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		Contrato contrato = contratoRepository.buscarPorIdNoEliminado(id)
				.orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));

		if (contrato.getEstado() != EstadoContrato.BORRADOR) {
			throw new IllegalArgumentException("Solo se pueden eliminar contratos en estado BORRADOR");
		}

		contrato.setEliminado(true);
		contratoRepository.save(contrato);
	}

	@Override
	public List<Contrato> obtenerActivos() {
		return contratoRepository.listarActivos();
	}

	@Override
	public List<Contrato> filtrar(Long propiedadId, Long inquilinoId, EstadoContrato estado, LocalDate fechaInicio) {
		return contratoRepository.filtrar(propiedadId, inquilinoId, estado, fechaInicio);
	}

	@Override
	@Transactional
	public void editar(Long id, ContratoForm form) {
		validarCampos(form);

		Contrato contrato = contratoRepository.buscarPorIdNoEliminado(id)
				.orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));

		EstadoContrato nuevoEstado = form.getEstado();
		if (nuevoEstado == null) {
			nuevoEstado = contrato.getEstado();
		}

		validarCambioDeEstado(contrato.getEstado(), nuevoEstado);

		Propiedad propiedad = propiedadRepository.buscarPorIdNoEliminada(form.getPropiedadId())
				.orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada o eliminada"));

		if (contrato.getEstado() != EstadoContrato.BORRADOR) {
			if (!propiedad.getId().equals(contrato.getPropiedad().getId())) {
				throw new IllegalArgumentException("No se puede cambiar la propiedad en contratos " + contrato.getEstado());
			}
		}

		if (contrato.getEstado() != EstadoContrato.BORRADOR) {
			if (!contrato.getFechaInicio().equals(form.getFechaInicio())) {
				throw new IllegalArgumentException("No se puede cambiar la fecha de inicio en contratos " + contrato.getEstado());
			}
			if (!contrato.getInquilino().getId().equals(form.getInquilinoId())) {
				throw new IllegalArgumentException("No se puede cambiar el inquilino en contratos " + contrato.getEstado());
			}
			if (!contrato.getDiaVencimientoMensual().equals(form.getDiaVencimientoMensual())) {
				throw new IllegalArgumentException("No se puede cambiar el día de vencimiento en contratos " + contrato.getEstado());
			}
		}

		if (nuevoEstado == EstadoContrato.ACTIVO && contrato.getEstado() != EstadoContrato.ACTIVO) {
			validarActivacionContrato(propiedad);
		}

		EstadoContrato estadoAnterior = contrato.getEstado();

		contrato.setPropiedad(propiedad);
		contrato.setFechaInicio(form.getFechaInicio());
		contrato.setDuracionMeses(form.getDuracionMeses());
		contrato.setImporteMensual(form.getImporteMensual());
		contrato.setDiaVencimientoMensual(form.getDiaVencimientoMensual());
		contrato.setDescripcion(form.getDescripcion());
		contrato.setInquilino(personaRepository.buscarActivaPorId(form.getInquilinoId())
				.orElseThrow(() -> new IllegalArgumentException("Inquilino no encontrado o eliminado")));
		contrato.setEstado(nuevoEstado);

		if (nuevoEstado != estadoAnterior) {
			HistorialEstadoContrato nuevoHistorial = new HistorialEstadoContrato();
			nuevoHistorial.setEstado(nuevoEstado);
			nuevoHistorial.setFechaHora(LocalDateTime.now());
			nuevoHistorial.setContrato(contrato);
			contrato.getHistorialEstados().add(nuevoHistorial);

			if (nuevoEstado == EstadoContrato.ACTIVO) {
				propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.ALQUILADA);

				HistorialEstadoPropiedad historialPropiedad = new HistorialEstadoPropiedad();
				historialPropiedad.setEstado(EstadoDisponibilidad.ALQUILADA);
				historialPropiedad.setFechaHora(LocalDateTime.now());
				historialPropiedad.setPropiedad(propiedad);
				propiedad.getHistorialEstados().add(historialPropiedad);
			} else if (nuevoEstado == EstadoContrato.FINALIZADO || nuevoEstado == EstadoContrato.RESCINDIDO) {
				propiedad.setEstadoDisponibilidad(EstadoDisponibilidad.DISPONIBLE);

				HistorialEstadoPropiedad historialPropiedad = new HistorialEstadoPropiedad();
				historialPropiedad.setEstado(EstadoDisponibilidad.DISPONIBLE);
				historialPropiedad.setFechaHora(LocalDateTime.now());
				historialPropiedad.setPropiedad(propiedad);
				propiedad.getHistorialEstados().add(historialPropiedad);
			}
		}

		contratoRepository.save(contrato);
	}

	@Override
	public Contrato obtenerPorId(Long id) {
		return contratoRepository.buscarPorIdNoEliminado(id)
				.orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));
	}

	private void validarCambioDeEstado(EstadoContrato estadoActual, EstadoContrato estadoNuevo) {
		if (estadoActual == estadoNuevo) {
			return;
		}

		boolean cambioValido = false;

		if (estadoActual == EstadoContrato.BORRADOR && estadoNuevo == EstadoContrato.ACTIVO) {
			cambioValido = true;
		} else if (estadoActual == EstadoContrato.ACTIVO &&
				   (estadoNuevo == EstadoContrato.FINALIZADO || estadoNuevo == EstadoContrato.RESCINDIDO)) {
			cambioValido = true;
		}

		if (!cambioValido) {
			throw new IllegalArgumentException("Cambio de estado no permitido: " + estadoActual + " -> " + estadoNuevo);
		}
	}

	private void validarActivacionContrato(Propiedad propiedad) {
		if (propiedad.getEstadoDisponibilidad() != EstadoDisponibilidad.DISPONIBLE) {
			throw new IllegalArgumentException("No se puede crear un contrato activo si la propiedad no está disponible");
		}

		boolean existeContratoActivo = contratoRepository.tieneContratoActivo(propiedad.getId());

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