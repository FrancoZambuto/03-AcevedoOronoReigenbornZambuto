package com.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.entidades.Contrato;
import com.desi.entidades.EstadoContrato;
import com.desi.servicios.ContratoService;
import com.desi.servicios.PropiedadService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/contrato")
public class ContratoController {

	@Autowired
	private ContratoService contratoService;

	@Autowired
	private PropiedadService propiedadService;

	@GetMapping("/listado")
	public String listado(
			@RequestParam(required = false) Long propiedadId,
			@RequestParam(required = false) Long inquilinoId,
			@RequestParam(required = false) EstadoContrato estado,
			@RequestParam(required = false) String fechaInicio,
			Model model) {

		java.time.LocalDate fecha = null;
		if (fechaInicio != null && !fechaInicio.isBlank()) {
			fecha = java.time.LocalDate.parse(fechaInicio);
		}

		List<Contrato> contratos;
		if (propiedadId != null || inquilinoId != null || estado != null || fecha != null) {
			contratos = contratoService.filtrar(propiedadId, inquilinoId, estado, fecha);
		} else {
			contratos = contratoService.obtenerActivos();
		}

		model.addAttribute("contratos", contratos);
		model.addAttribute("propiedadIdFiltro", propiedadId);
		model.addAttribute("inquilinoIdFiltro", inquilinoId);
		model.addAttribute("estadoFiltro", estado);
		model.addAttribute("fechaInicioFiltro", fechaInicio);
		model.addAttribute("allPropiedades", propiedadService.obtenerTodasActivas());
		model.addAttribute("allInquilinos", contratoService.obtenerInquilinos());
		model.addAttribute("estadosContrato", EstadoContrato.values());

		return "listadoContratos";
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			contratoService.eliminar(id);
			redirectAttributes.addFlashAttribute("mensajeExito", true);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		}
		return "redirect:/contrato/listado";
	}

	@GetMapping("/alta")
	public String preparaForm(Model model, @RequestParam(required = false) Boolean exito) {
		model.addAttribute("contratoForm", new ContratoForm());

		if (exito != null && exito) {
			model.addAttribute("mensajeExito", true);
		}

		cargarListas(model);
		return "altaContrato";
	}

	@PostMapping("/alta")
	public String submit(@Valid @ModelAttribute("contratoForm") ContratoForm contratoForm,
						 BindingResult result,
						 Model model) {

		if (result.hasErrors()) {
			cargarListas(model);
			return "altaContrato";
		}

		try {
			contratoService.registrar(contratoForm);
		} catch (IllegalArgumentException e) {
			result.reject("contrato.error", e.getMessage());
			cargarListas(model);
			return "altaContrato";
		}

		return "redirect:/contrato/alta?exito=true";
	}

	private void cargarListas(Model model) {
		model.addAttribute("allPropiedades", contratoService.obtenerPropiedadesDisponibles());
		model.addAttribute("allInquilinos", contratoService.obtenerInquilinos());
		model.addAttribute("estadosContrato", EstadoContrato.values());
	}
}