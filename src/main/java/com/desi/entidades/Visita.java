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
@Table(name = "VISITA")
public class Visita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime fechaHora;

	@Enumerated(EnumType.STRING)
	private EstadoVisita estado;

	@ManyToOne
	@JoinColumn(name = "publicacion_id")
	private Publicacion publicacion;

	public Visita() {
	}

	public Visita(Long id, LocalDateTime fechaHora, EstadoVisita estado, Publicacion publicacion) {
		this.id = id;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.publicacion = publicacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public EstadoVisita getEstado() {
		return estado;
	}

	public void setEstado(EstadoVisita estado) {
		this.estado = estado;
	}

	public Publicacion getPublicacion() {
		return publicacion;
	}

	public void setPublicacion(Publicacion publicacion) {
		this.publicacion = publicacion;
	}
}
