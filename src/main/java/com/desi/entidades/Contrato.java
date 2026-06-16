package com.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTRATO")
public class Contrato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fechaInicio;

	private Integer duracionMeses;

	private BigDecimal importeMensual;

	private Integer diaVencimientoMensual;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private EstadoContrato estado;

	private boolean eliminado;

	@ManyToOne
	@JoinColumn(name = "propiedad_id")
	private Propiedad propiedad;

	@ManyToOne
	@JoinColumn(name = "inquilino_id")
	private Persona inquilino;

	@OneToMany(mappedBy = "contrato")
	private List<Factura> facturas = new ArrayList<>();

	@OneToMany(mappedBy = "contrato")
	private List<Incidente> incidentes = new ArrayList<>();

	@OneToMany(mappedBy = "contrato")
	private List<HistorialEstadoContrato> historialEstados = new ArrayList<>();

	public Contrato() {
	}

	public Contrato(Long id, LocalDate fechaInicio, Integer duracionMeses, BigDecimal importeMensual,
			Integer diaVencimientoMensual, String descripcion, EstadoContrato estado, boolean eliminado,
			Propiedad propiedad, Persona inquilino) {
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.duracionMeses = duracionMeses;
		this.importeMensual = importeMensual;
		this.diaVencimientoMensual = diaVencimientoMensual;
		this.descripcion = descripcion;
		this.estado = estado;
		this.eliminado = eliminado;
		this.propiedad = propiedad;
		this.inquilino = inquilino;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public List<Incidente> getIncidentes() {
		return incidentes;
	}

	public void setIncidentes(List<Incidente> incidentes) {
		this.incidentes = incidentes;
	}

	public List<HistorialEstadoContrato> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoContrato> historialEstados) {
		this.historialEstados = historialEstados;
	}
}
