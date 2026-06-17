package com.desi.presentacion;

import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.TipoPropiedad;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PropiedadForm {

	@NotEmpty(message = "{propiedad.direccion.notEmpty}")
	private String direccion;

	@NotNull(message = "{propiedad.tipo.notNull}")
	private TipoPropiedad tipo;

	@NotNull(message = "{propiedad.cantidadAmbientes.notNull}")
	@Positive(message = "{propiedad.cantidadAmbientes.positive}")
	private Integer cantidadAmbientes;

	@NotNull(message = "{propiedad.metrosCuadrados.notNull}")
	@Positive(message = "{propiedad.metrosCuadrados.positive}")
	private Double metrosCuadrados;

	@NotEmpty(message = "{propiedad.descripcion.notEmpty}")
	private String descripcion;

	@NotEmpty(message = "{propiedad.comodidades.notEmpty}")
	private String comodidades;

	private EstadoDisponibilidad estadoDisponibilidad;

	@NotNull(message = "{propiedad.ciudad.notNull}")
	private Long ciudadId;

	@NotNull(message = "{propiedad.propietario.notNull}")
	private Long propietarioId;

	public PropiedadForm() {
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public TipoPropiedad getTipo() {
		return tipo;
	}

	public void setTipo(TipoPropiedad tipo) {
		this.tipo = tipo;
	}

	public Integer getCantidadAmbientes() {
		return cantidadAmbientes;
	}

	public void setCantidadAmbientes(Integer cantidadAmbientes) {
		this.cantidadAmbientes = cantidadAmbientes;
	}

	public Double getMetrosCuadrados() {
		return metrosCuadrados;
	}

	public void setMetrosCuadrados(Double metrosCuadrados) {
		this.metrosCuadrados = metrosCuadrados;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getComodidades() {
		return comodidades;
	}

	public void setComodidades(String comodidades) {
		this.comodidades = comodidades;
	}

	public EstadoDisponibilidad getEstadoDisponibilidad() {
		return estadoDisponibilidad;
	}

	public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {
		this.estadoDisponibilidad = estadoDisponibilidad;
	}

	public Long getCiudadId() {
		return ciudadId;
	}

	public void setCiudadId(Long ciudadId) {
		this.ciudadId = ciudadId;
	}

	public Long getPropietarioId() {
		return propietarioId;
	}

	public void setPropietarioId(Long propietarioId) {
		this.propietarioId = propietarioId;
	}
}
