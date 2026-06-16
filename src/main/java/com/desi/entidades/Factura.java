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
@Table(name = "FACTURA")
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fechaEmision;

	private LocalDate fechaVencimiento;

	private BigDecimal importe;

	@Enumerated(EnumType.STRING)
	private EstadoFactura estado;

	private boolean eliminada;

	private LocalDate fechaPago;

	@Enumerated(EnumType.STRING)
	private MedioPago medio;

	private BigDecimal importePagado;

	private BigDecimal interes;

	private String conceptoFacturado;

	@ManyToOne
	@JoinColumn(name = "contrato_id")
	private Contrato contrato;

	@OneToMany(mappedBy = "factura")
	private List<HistorialEstadoFactura> historialEstados = new ArrayList<>();

	public Factura() {
	}

	public Factura(Long id, LocalDate fechaEmision, LocalDate fechaVencimiento, BigDecimal importe,
			EstadoFactura estado, boolean eliminada, LocalDate fechaPago, MedioPago medio, BigDecimal importePagado,
			BigDecimal interes, String conceptoFacturado, Contrato contrato) {
		this.id = id;
		this.fechaEmision = fechaEmision;
		this.fechaVencimiento = fechaVencimiento;
		this.importe = importe;
		this.estado = estado;
		this.eliminada = eliminada;
		this.fechaPago = fechaPago;
		this.medio = medio;
		this.importePagado = importePagado;
		this.interes = interes;
		this.conceptoFacturado = conceptoFacturado;
		this.contrato = contrato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public MedioPago getMedio() {
		return medio;
	}

	public void setMedio(MedioPago medio) {
		this.medio = medio;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public BigDecimal getInteres() {
		return interes;
	}

	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}

	public String getConceptoFacturado() {
		return conceptoFacturado;
	}

	public void setConceptoFacturado(String conceptoFacturado) {
		this.conceptoFacturado = conceptoFacturado;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public List<HistorialEstadoFactura> getHistorialEstados() {
		return historialEstados;
	}

	public void setHistorialEstados(List<HistorialEstadoFactura> historialEstados) {
		this.historialEstados = historialEstados;
	}
}
