package com.desi.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.desi.entidades.EstadoContrato;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ContratoForm {

	@NotNull(message = "{contrato.propiedad.notNull}")
	private Long propiedadId;

	@NotNull(message = "{contrato.inquilino.notNull}")
	private Long inquilinoId;

	@NotNull(message = "{contrato.fechaInicio.notNull}")
	private LocalDate fechaInicio;

	@NotNull(message = "{contrato.duracionMeses.notNull}")
	@Min(value = 1, message = "{contrato.duracionMeses.positive}")
	private Integer duracionMeses;

	@NotNull(message = "{contrato.importeMensual.notNull}")
	@DecimalMin(value = "0.01", message = "{contrato.importeMensual.positive}")
	private BigDecimal importeMensual;

	@NotNull(message = "{contrato.diaVencimientoMensual.notNull}")
	@Min(value = 1, message = "{contrato.diaVencimientoMensual.min}")
	@Max(value = 31, message = "{contrato.diaVencimientoMensual.max}")
	private Integer diaVencimientoMensual;

	@NotEmpty(message = "{contrato.descripcion.notEmpty}")
	private String descripcion;

	private EstadoContrato estado;

	public ContratoForm() {
	}

	public Long getPropiedadId() {
		return propiedadId;
	}

	public void setPropiedadId(Long propiedadId) {
		this.propiedadId = propiedadId;
	}

	public Long getInquilinoId() {
		return inquilinoId;
	}

	public void setInquilinoId(Long inquilinoId) {
		this.inquilinoId = inquilinoId;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(Integer duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}

	public void setEstado(EstadoContrato estado) {
		this.estado = estado;
	}
}