package com.desi.presentacion.personas;

import com.desi.servicios.PersonaService;
import org.springframework.ui.Model;
import com.desi.entidades.Ciudad;
import com.desi.entidades.Persona;
import com.desi.servicios.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/personasBuscar")
public class PersonasBuscarController {
    @Autowired
    private PersonaService service;
    @Autowired
    private CiudadService serviceCiudad;

    @RequestMapping(method=RequestMethod.GET)
    public String preparaForm(Model modelo) {
        PersonasBuscarForm form =  new PersonasBuscarForm();
//   	form.setCiudades(serviceCiudad.getAll());    //  en lugar de esto hacemos @ModelAttribute("allCiudades")
        modelo.addAttribute("formBean",form);
        return "personasBuscar";
    }

    @ModelAttribute("allCiudades")
    public List<Ciudad> getAllCiudades() {
        return serviceCiudad.getAll();
    }

    @RequestMapping( method= RequestMethod.POST)
    public String submit(PersonasBuscarForm formBean, BindingResult result, ModelMap modelo, @RequestParam String action) {


        if(action.equals("Buscar"))
        {


            List<Persona> personas = service.filter(formBean);

            modelo.addAttribute("formBean",formBean);
            modelo.addAttribute("resultados",personas);
            return "personasBuscar";
        }


        if(action.equals("Cancelar"))
        {
            modelo.clear();
            return "redirect:/";
        }

        if(action.equals("Registrar"))
        {
            modelo.clear();
            return "redirect:/personasEditar";
        }

        return "redirect:/";


    }
}
