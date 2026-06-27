package com.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.desi.entidades.Factura;

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

    @GetMapping("/{id}/editar")
    public String preparaEdicionForm(@PathVariable Long id,
            Model model,
            @RequestParam(required = false) Boolean exito) {

        Factura factura = facturaService.obtenerPorId(id);

        FacturaForm form = new FacturaForm();
        form.setContratoId(factura.getContrato().getId());
        form.setConceptoFacturado(factura.getConceptoFacturado());
        form.setFechaEmision(factura.getFechaEmision());
        form.setFechaVencimiento(factura.getFechaVencimiento());
        form.setImporte(factura.getImporte());
        form.setEstado(factura.getEstado());
        form.setFechaPago(factura.getFechaPago());
        form.setMedio(factura.getMedio());
        form.setImportePagado(factura.getImportePagado());
        form.setInteres(factura.getInteres());

        model.addAttribute("facturaId", id);
        model.addAttribute("facturaForm", form);

        if (exito != null && exito) {
            model.addAttribute("mensajeExito", true);
        }

        cargarListas(model);
        return "editarFactura";
    }

    @PostMapping("/{id}/editar")
    public String submitEdicion(@PathVariable Long id,
            @Valid @ModelAttribute("facturaForm") FacturaForm form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("facturaId", id);
            cargarListas(model);
            return "editarFactura";
        }

        try {
            facturaService.editar(id, form);
        } catch (IllegalArgumentException e) {
            result.reject("factura.error", e.getMessage());
            model.addAttribute("facturaId", id);
            cargarListas(model);
            return "editarFactura";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", true);
        return "redirect:/factura/" + id + "/editar";
    }
    private void cargarListas(Model model) {
        model.addAttribute("contratos", facturaService.obtenerContratosActivos());
        model.addAttribute("estadosFactura", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
    }
}