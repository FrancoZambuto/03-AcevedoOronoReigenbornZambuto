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

import com.desi.entidades.EstadoPublicacion;
import com.desi.entidades.Publicacion;
import com.desi.excepciones.PublicacionActivaExistenteException;
import com.desi.excepciones.PublicacionNoEliminableException;
import com.desi.servicios.PublicacionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/publicacion")
public class PublicacionController {

	@Autowired
	private PublicacionService publicacionService;

	@GetMapping("/listado")
	public String listar(Model model) {
		model.addAttribute("publicaciones", publicacionService.listarNoEliminadas());
		return "listarPublicaciones";
	}

	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			publicacionService.eliminar(id);
			redirectAttributes.addFlashAttribute("mensajeExito", true);
		} catch (PublicacionNoEliminableException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
		}
		return "redirect:/publicacion/listado";
	}

	@GetMapping("/{id}/editar")
	public String preparaEdicionForm(@PathVariable Long id, Model model,
			@RequestParam(required = false) Boolean exito) {
		Publicacion publicacion = publicacionService.buscarPorId(id);
		model.addAttribute("publicacion", publicacion);
		model.addAttribute("publicacionForm", publicacionService.buscarParaEdicion(id));
		model.addAttribute("publicacionId", id);
		String fechaStr = publicacion.getFechaPublicacion() != null
				? publicacion.getFechaPublicacion().toString()
				: "";
		model.addAttribute("fechaPublicacionStr", fechaStr);
		if (exito != null && exito) {
			model.addAttribute("mensajeExito", true);
		}
		model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
		return "editarPublicacion";
	}

	@PostMapping("/{id}/editar")
	public String submitEdicion(@PathVariable Long id,
			@Valid @ModelAttribute("publicacionForm") PublicacionForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("publicacion", publicacionService.buscarPorId(id));
			model.addAttribute("publicacionId", id);
			model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
			return "editarPublicacion";
		}

		try {
			publicacionService.actualizar(id, form);
		} catch (PublicacionActivaExistenteException e) {
			result.rejectValue("estado", "publicacion.activa.existente", e.getMessage());
			model.addAttribute("publicacion", publicacionService.buscarPorId(id));
			model.addAttribute("publicacionId", id);
			model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
			return "editarPublicacion";
		} catch (IllegalArgumentException e) {
			result.reject("publicacion.error", e.getMessage());
			model.addAttribute("publicacion", publicacionService.buscarPorId(id));
			model.addAttribute("publicacionId", id);
			model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
			return "editarPublicacion";
		}

		redirectAttributes.addFlashAttribute("mensajeEdicionExito", true);
		return "redirect:/publicacion/listado";
	}

	@GetMapping("/alta")
	public String preparaForm(Model model, @RequestParam(required = false) Boolean exito) {
		model.addAttribute("publicacionForm", new PublicacionForm());
		if (exito != null && exito) {
			model.addAttribute("mensajeExito", true);
		}
		cargarListas(model);
		return "altaPublicacion";
	}

	@PostMapping("/alta")
	public String submit(@Valid @ModelAttribute("publicacionForm") PublicacionForm form,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			cargarListas(model);
			return "altaPublicacion";
		}

		try {
			publicacionService.registrar(form);
		} catch (PublicacionActivaExistenteException e) {
			result.rejectValue("propiedadId", "publicacion.activa.existente", e.getMessage());
			cargarListas(model);
			return "altaPublicacion";
		} catch (IllegalArgumentException e) {
			result.reject("publicacion.error", e.getMessage());
			cargarListas(model);
			return "altaPublicacion";
		}

		return "redirect:/publicacion/alta?exito=true";
	}

	private void cargarListas(Model model) {
		model.addAttribute("propiedadesDisponibles", publicacionService.obtenerPropiedadesDisponibles());
		model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
	}
}
