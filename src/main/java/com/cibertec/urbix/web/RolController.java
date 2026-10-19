package com.cibertec.urbix.web;

import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/roles")
public class RolController {

    private final RolService roles;

    public RolController(RolService roles) {
        this.roles = roles;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("lista", roles.listar());
        return "roles";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, Model modelo) {
        try {
            roles.registrar(nombre);
        } catch (DatosInvalidosException e) {
            modelo.addAttribute("lista", roles.listar());
            modelo.addAttribute("error", e.getMessage());
            return "roles";
        }
        return "redirect:/roles";
    }

    @PostMapping("/{id}/estado")
    public String estado(@PathVariable Integer id, @RequestParam boolean activo) {
        roles.cambiarActivo(id, activo);
        return "redirect:/roles";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, Model modelo) {
        try {
            roles.eliminar(id);
        } catch (RuntimeException e) {
            modelo.addAttribute("lista", roles.listar());
            modelo.addAttribute("error", "No se puede borrar: ya se usa en usuarios. Desactívelo.");
            return "roles";
        }
        return "redirect:/roles";
    }
}