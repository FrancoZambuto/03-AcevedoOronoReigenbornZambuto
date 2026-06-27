package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Contrato;
import com.desi.entidades.Factura;
import com.desi.presentacion.FacturaForm;

public interface FacturaService {

    void registrar(FacturaForm form);

    void editar(Long id, FacturaForm form);

    Factura obtenerPorId(Long id);

    List<Contrato> obtenerContratosActivos();
}