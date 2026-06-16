package com.desi.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
@Table(name = "PROPIEDAD")
public class Propiedad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String direccion;

	@Enumerated(EnumType.STRING)
	private TipoPropiedad tipo;

	private Integer cantidadAmbientes;

	private Double metrosCuadrados;

	private String descripcion;

	private String comodidades;

	@Enumerated(EnumType.STRING)
	private EstadoDisponibilidad estadoDisponibilidad;

	private boolean eliminada;

	@ManyToOne
	@JoinColumn(name = "ciudad_id")
	private Ciudad ciudad;

	@ManyToOne
	@JoinColumn(name = "propietario_id")
	private Persona propietario;

	@OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
	private List<HistorialEstadoPropiedad> historialEstados = new ArrayList<>();

	@OneToMany(mappedBy = "propiedad")
	private List<Publicacion> publicaciones = new ArrayList<>();

	@OneToMany(mappedBy = "propiedad")
	private List<Contrato> contratos = new ArrayList<>();

	public Propiedad() {
	}

	public Propiedad(Long id, String direccion, TipoPropiedad tipo, Integer cantidadAmbientes, Double metrosCuadrados,
			String descripcion, String comodidades, EstadoDisponibilidad estadoDisponibilidad, boolean eliminada,
			Ciudad ciudad, Persona propietario) {
		this.id = id;
		this.direccion = direccion;
		this.tipo = tipo;
		this.cantidadAmbientes = cantidadAmbientes;
		this.metrosCuadrados = metrosCuadrados;
		this.descripcion = descripcion;
		this.comodidades = comodidades;
		this.estadoDisponibilidad = estadoDisponibilidad;
		this.eliminada = eliminada;
		this.ciudad = ciudad;
		this.propietario = propietario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public Persona getPropietario() {
		return propietario;
	}

	public void setPropietario(Persona propietario) {
		this.propietario = propietario;
	}

	public List<HistorialEstadoPropiedad> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoPropiedad> historialEstados) {
		this.historialEstados = historialEstados;
	}

	public List<Publicacion> getPublicaciones() {
		return publicaciones;
	}

	public void setPublicaciones(List<Publicacion> publicaciones) {
		this.publicaciones = publicaciones;
	}

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}
}
