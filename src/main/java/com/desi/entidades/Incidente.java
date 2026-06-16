package com.desi.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "INCIDENTE")
public class Incidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private Categoriaincidente categoria;

	private LocalDateTime fechaAlta;

	@Enumerated(EnumType.STRING)
	private Prioridadincidente prioridad;

	@Enumerated(EnumType.STRING)
	private Estadoincidente estado;

	private boolean eliminado;

	private LocalDateTime fechaResolucion;

	private String observacionesResolucion;

	private BigDecimal costoResolucion;

	private String responsableTecnico;

	@ManyToOne
	@JoinColumn(name = "contrato_id")
	private Contrato contrato;

	@OneToMany(mappedBy = "incidente")
	private List<HistorialEstadoincidente> historialEstados = new ArrayList<>();

	public Incidente() {
	}

	public Incidente(Long id, String titulo, String descripcion, Categoriaincidente categoria,
			LocalDateTime fechaAlta, Prioridadincidente prioridad, Estadoincidente estado, boolean eliminado,
			LocalDateTime fechaResolucion, String observacionesResolucion, BigDecimal costoResolucion,
			String responsableTecnico, Contrato contrato) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.fechaAlta = fechaAlta;
		this.prioridad = prioridad;
		this.estado = estado;
		this.eliminado = eliminado;
		this.fechaResolucion = fechaResolucion;
		this.observacionesResolucion = observacionesResolucion;
		this.costoResolucion = costoResolucion;
		this.responsableTecnico = responsableTecnico;
		this.contrato = contrato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Categoriaincidente getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoriaincidente categoria) {
		this.categoria = categoria;
	}

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Prioridadincidente getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Prioridadincidente prioridad) {
		this.prioridad = prioridad;
	}

	public Estadoincidente getEstado() {
		return estado;
	}

	public void setEstado(Estadoincidente estado) {
		this.estado = estado;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public LocalDateTime getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(LocalDateTime fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getObservacionesResolucion() {
		return observacionesResolucion;
	}

	public void setObservacionesResolucion(String observacionesResolucion) {
		this.observacionesResolucion = observacionesResolucion;
	}

	public BigDecimal getCostoResolucion() {
		return costoResolucion;
	}

	public void setCostoResolucion(BigDecimal costoResolucion) {
		this.costoResolucion = costoResolucion;
	}

	public String getResponsableTecnico() {
		return responsableTecnico;
	}

	public void setResponsableTecnico(String responsableTecnico) {
		this.responsableTecnico = responsableTecnico;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<HistorialEstadoincidente> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoincidente> historialEstados) {
		this.historialEstados = historialEstados;
	}
}
