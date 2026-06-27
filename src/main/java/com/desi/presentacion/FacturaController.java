package com.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.desi.entidades.EstadoFactura;
import com.desi.entidades.MedioPago;
import com.desi.servicios.FacturaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/factura")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/alta")
    public String preparaForm(Model model) {
        model.addAttribute("facturaForm", new FacturaForm());
        cargarListas(model);
        return "altaFactura";
    }

    @PostMapping("/alta")
    public String submit(
            @Valid @ModelAttribute("facturaForm") FacturaForm form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            cargarListas(model);
            return "altaFactura";
        }

        try {
            facturaService.registrar(form);
        } catch (IllegalArgumentException e) {
            result.reject("factura.error", e.getMessage());
            cargarListas(model);
            return "altaFactura";
        }

        return "redirect:/factura/alta?exito=true";
    }

    private void cargarListas(Model model) {
        model.addAttribute("contratos", facturaService.obtenerContratosActivos());
        model.addAttribute("estadosFactura", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
    }
}