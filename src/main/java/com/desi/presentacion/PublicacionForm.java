package com.desi.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.desi.entidades.EstadoPublicacion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PublicacionForm {

	@NotNull(message = "{publicacion.propiedad.notNull}")
	private Long propiedadId;

	@NotNull(message = "{publicacion.precioMensual.notNull}")
	@DecimalMin(value = "0.01", message = "{publicacion.precioMensual.positive}")
	private BigDecimal precioMensual;

	@NotEmpty(message = "{publicacion.condiciones.notEmpty}")
	private String condiciones;

	@NotEmpty(message = "{publicacion.descripcion.notEmpty}")
	private String descripcion;

	@NotNull(message = "{publicacion.fechaPublicacion.notNull}")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaPublicacion;

	private EstadoPublicacion estado;

	public PublicacionForm() {
	}

	public Long getPropiedadId() {
		return propiedadId;
	}

	public void setPropiedadId(Long propiedadId) {
		this.propiedadId = propiedadId;
	}

	public BigDecimal getPrecioMensual() {
		return precioMensual;
	}

	public void setPrecioMensual(BigDecimal precioMensual) {
		this.precioMensual = precioMensual;
	}

	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public EstadoPublicacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoPublicacion estado) {
		this.estado = estado;
	}
}
