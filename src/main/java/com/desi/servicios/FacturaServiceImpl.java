package com.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.accesoDatos.ContratoRepository;
import com.desi.accesoDatos.FacturaRepository;
import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;
import com.desi.entidades.EstadoFactura;
import com.desi.entidades.Factura;
import com.desi.entidades.HistorialEstadoFactura;
import com.desi.presentacion.FacturaForm;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Override
    @Transactional
    public void registrar(FacturaForm form) {

        validarCampos(form);

        Contrato contrato = contratoRepository.buscarPorIdNoEliminado(form.getContratoId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new IllegalArgumentException(
                    "No se puede crear una factura para un contrato que no esté activo");
        }

        Factura factura = new Factura();
        factura.setContrato(contrato);
        factura.setConceptoFacturado(form.getConceptoFacturado());
        factura.setFechaEmision(form.getFechaEmision());
        factura.setFechaVencimiento(form.getFechaVencimiento());
        factura.setImporte(form.getImporte());
        EstadoFactura estado = form.getEstado();
        if (estado == null) {
            estado = EstadoFactura.PENDIENTE;
        }

        factura.setEstado(estado);
        factura.setEliminada(false);

        factura.setFechaPago(form.getFechaPago());
        factura.setMedio(form.getMedio());
        factura.setImportePagado(form.getImportePagado());
        factura.setInteres(form.getInteres());

        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setEstado(estado);
        historial.setFechaHora(LocalDateTime.now());
        historial.setFactura(factura);

        factura.getHistorialEstados().add(historial);

        facturaRepository.save(factura);
    }

    @Override
    public List<Contrato> obtenerContratosActivos() {
        return facturaRepository.listarContratosActivos(EstadoContrato.ACTIVO);
    }

    private void validarCampos(FacturaForm form) {

        if (form.getContratoId() == null) {
            throw new IllegalArgumentException("El contrato es obligatorio");
        }

        if (form.getConceptoFacturado() == null || form.getConceptoFacturado().isBlank()) {
            throw new IllegalArgumentException("El concepto facturado es obligatorio");
        }

        if (form.getFechaEmision() == null) {
            throw new IllegalArgumentException("La fecha de emisión es obligatoria");
        }

        if (form.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria");
        }

        if (form.getFechaVencimiento().isBefore(form.getFechaEmision())) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento debe ser igual o posterior a la fecha de emisión");
        }

        if (form.getImporte() == null
                || form.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo");
        }

        if (form.getImportePagado() != null
                && form.getImportePagado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe pagado debe ser positivo");
        }

        if (form.getInteres() != null
                && form.getInteres().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El interés pagado debe ser positivo");
        }
    }
}