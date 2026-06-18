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

import com.desi.entidades.EstadoDisponibilidad;
import com.desi.entidades.TipoPropiedad;
import com.desi.excepciones.PropiedadConContratoActivoException;
import com.desi.excepciones.PropiedadDuplicadaException;
import com.desi.servicios.PropiedadService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/propiedad")
public class PropiedadController {

	@Autowired
	private PropiedadService propiedadService;

	@GetMapping("/listado")
	public String listado(Model model) {
		model.addAttribute("propiedades", propiedadService.obtenerActivas());
		return "listadoPropiedades";
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			propiedadService.eliminar(id);
			redirectAttributes.addFlashAttribute("mensajeExito", true);
		} catch (PropiedadConContratoActivoException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		}
		return "redirect:/propiedad/listado";
	}

	@GetMapping("/alta")
	public String preparaForm(Model model, @RequestParam(required = false) Boolean exito) {
		model.addAttribute("propiedadForm", new PropiedadForm());
		if (exito != null && exito) {
			model.addAttribute("mensajeExito", true);
		}
		cargarListas(model);
		return "altaPropiedad";
	}

	@PostMapping("/alta")
	public String submit(@Valid @ModelAttribute("propiedadForm") PropiedadForm propiedadForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			cargarListas(model);
			return "altaPropiedad";
		}

		try {
			propiedadService.registrar(propiedadForm);
		} catch (PropiedadDuplicadaException e) {
			result.rejectValue("direccion", "propiedad.duplicada", e.getMessage());
			cargarListas(model);
			return "altaPropiedad";
		} catch (IllegalArgumentException e) {
			result.reject("propiedad.error", e.getMessage());
			cargarListas(model);
			return "altaPropiedad";
		}

		return "redirect:/propiedad/alta?exito=true";
	}

	private void cargarListas(Model model) {
		model.addAttribute("allPropietarios", propiedadService.obtenerPropietarios());
		model.addAttribute("allCiudades", propiedadService.obtenerCiudades());
		model.addAttribute("tiposPropiedad", TipoPropiedad.values());
		model.addAttribute("estadosDisponibilidad", EstadoDisponibilidad.values());
	}
}
