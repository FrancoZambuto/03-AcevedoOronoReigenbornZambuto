package com.desi.servicios;

import java.util.List;

import com.desi.entidades.Contrato;
import com.desi.presentacion.FacturaForm;

public interface FacturaService {

    void registrar(FacturaForm form);

    List<Contrato> obtenerContratosActivos();
}