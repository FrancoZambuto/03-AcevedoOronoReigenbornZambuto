package com.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.desi.entidades.EstadoContrato;
import com.desi.servicios.ContratoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contrato")
public class ContratoController {

	@Autowired
	private ContratoService contratoService;

	@GetMapping("/listado")
	public String listado(Model model) {
		model.addAttribute("contratos", contratoService.obtenerActivos());
		return "listadoContratos";
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