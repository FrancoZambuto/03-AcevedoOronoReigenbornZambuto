package com.desi.servicios;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desi.accesoDatos.ContratoRepository;
import com.desi.accesoDatos.FacturaRepository;
import com.desi.accesoDatos.PropiedadRepository;
import com.desi.accesoDatos.PersonaRepository;
import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;
import com.desi.entidades.EstadoFactura;
import com.desi.entidades.Factura;
import com.desi.entidades.HistorialEstadoFactura;
import com.desi.entidades.Persona;
import com.desi.entidades.Propiedad;
import com.desi.presentacion.FacturaFiltroForm;
import com.desi.presentacion.FacturaForm;


@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;
    
    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Propiedad> obtenerPropiedades() {
        return propiedadRepository.listarActivas();
    }

    @Override
    public List<Persona> obtenerInquilinos() {
        return personaRepository.listarActivas();
    }
    
    @Override
    @Transactional
    public void registrar(FacturaForm form) {

        validarCampos(form);

        Contrato contrato = contratoRepository.buscarPorIdNoEliminado(form.getContratoId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato no encontrado"));

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new IllegalArgumentException(
                    "No se puede crear una factura para un contrato que no este activo");
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
    
    @Override
    public List<Factura> listarNoEliminadas() {
        return facturaRepository.listarNoEliminadas();
    }

    @Override
    public List<Factura> filtrar(FacturaFiltroForm filtro) {
        return facturaRepository.filtrar(
                filtro.getContratoId(),
                filtro.getPropiedadId(),
                filtro.getInquilinoId(),
                filtro.getEstado(),
                filtro.getFechaDesde(),
                filtro.getFechaHasta());
    }

    @Override
    public Factura obtenerPorId(Long id) {
        return facturaRepository.buscarPorIdNoEliminada(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));
    }

    @Override
    @Transactional
    public void editar(Long id, FacturaForm form) {
        validarCampos(form);

        Factura factura = facturaRepository.buscarPorIdNoEliminada(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalArgumentException(
                    "No se puede modificar una factura anulada");
        }

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException(
                    "No se puede modificar una factura pagada");
        }
        Contrato contrato = factura.getContrato();

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new IllegalArgumentException(
                    "Solo se pueden modificar facturas de contratos activos");
        }

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new IllegalArgumentException("Solo se pueden asociar facturas a contratos activos");
        }

        EstadoFactura estadoAnterior = factura.getEstado();

        EstadoFactura nuevoEstado = form.getEstado();
        if (nuevoEstado == null) {
            nuevoEstado = EstadoFactura.PENDIENTE;
        }
        if (estadoAnterior != nuevoEstado) {

            boolean cambioValido =
                    (estadoAnterior == EstadoFactura.PENDIENTE && nuevoEstado == EstadoFactura.PAGADA)
                 || (estadoAnterior == EstadoFactura.PENDIENTE && nuevoEstado == EstadoFactura.VENCIDA)
                 || (estadoAnterior == EstadoFactura.PENDIENTE && nuevoEstado == EstadoFactura.ANULADA)
                 || (estadoAnterior == EstadoFactura.VENCIDA && nuevoEstado == EstadoFactura.PAGADA);

            if (!cambioValido) {
                throw new IllegalArgumentException(
                        "El cambio de estado no es valido");
            }
        }
        if (nuevoEstado == EstadoFactura.PAGADA) {

            if (form.getFechaPago() == null) {
                throw new IllegalArgumentException(
                        "Debe ingresar la fecha de pago");
            }

            if (form.getMedio() == null) {
                throw new IllegalArgumentException(
                        "Debe ingresar el medio de pago");
            }

            if (form.getImportePagado() == null) {
                throw new IllegalArgumentException(
                        "Debe ingresar el importe pagado");
            }
        } else {

            if (form.getFechaPago() != null
                    || form.getMedio() != null
                    || form.getImportePagado() != null
                    || form.getInteres() != null) {

                throw new IllegalArgumentException(
                        "Solo se pueden registrar datos de pago para facturas pagadas");
            }
        }
        
        

        factura.setConceptoFacturado(form.getConceptoFacturado());
        factura.setFechaEmision(form.getFechaEmision());
        factura.setFechaVencimiento(form.getFechaVencimiento());
        factura.setImporte(form.getImporte());
        factura.setEstado(nuevoEstado);
        factura.setFechaPago(form.getFechaPago());
        factura.setMedio(form.getMedio());
        factura.setImportePagado(form.getImportePagado());
        factura.setInteres(form.getInteres());

        if (estadoAnterior != nuevoEstado) {
            HistorialEstadoFactura historial = new HistorialEstadoFactura();
            historial.setEstado(nuevoEstado);
            historial.setFechaHora(LocalDateTime.now());
            historial.setFactura(factura);
            factura.getHistorialEstados().add(historial);
        }

        facturaRepository.save(factura);
    }
    
    @Override
    @Transactional
    public void eliminar(Long id) {

        Factura factura = facturaRepository.buscarPorIdNoEliminada(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalArgumentException(
                    "No se puede eliminar una factura pagada");
        }

        factura.setEliminada(true);

        facturaRepository.save(factura);
    }
    
    private void validarCampos(FacturaForm form) {

        if (form.getContratoId() == null) {
            throw new IllegalArgumentException("El contrato es obligatorio");
        }

        if (form.getConceptoFacturado() == null || form.getConceptoFacturado().isBlank()) {
            throw new IllegalArgumentException("El concepto facturado es obligatorio");
        }

        if (form.getFechaEmision() == null) {
            throw new IllegalArgumentException("La fecha de emision es obligatoria");
        }

        if (form.getFechaVencimiento() == null) {
            throw new IllegalArgumentException("La fecha de vencimiento es obligatoria");
        }

        if (form.getFechaVencimiento().isBefore(form.getFechaEmision())) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento debe ser igual o posterior a la fecha de emision");
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
            throw new IllegalArgumentException("El interes pagado debe ser positivo");
        }
    }
}