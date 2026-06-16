package com.desi.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HISTORIAL_ESTADO_INCIDENTE")
public class HistorialEstadoincidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Estadoincidente estado;

	private LocalDateTime fechaHora;

	@ManyToOne
	@JoinColumn(name = "incidente_id")
	private Incidente incidente;

	public HistorialEstadoincidente() {
	}

	public HistorialEstadoincidente(Long id, Estadoincidente estado, LocalDateTime fechaHora, Incidente incidente) {
		this.id = id;
		this.estado = estado;
		this.fechaHora = fechaHora;
		this.incidente = incidente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estadoincidente getEstado() {
		return estado;
	}

	public void setEstado(Estadoincidente estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Incidente getIncidente() {
		return incidente;
	}

	public void setIncidente(Incidente incidente) {
		this.incidente = incidente;
	}
}
