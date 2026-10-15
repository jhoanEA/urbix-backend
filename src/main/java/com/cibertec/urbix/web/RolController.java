package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.repository.RolRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
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

    private final RolRepository roles;

    public RolController(RolRepository roles) {
        this.roles = roles;
    }

    private boolean sesion(HttpSession sesion) {
        return sesion.getAttribute("usuario") != null;
    }

    @GetMapping
    public String listar(HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        modelo.addAttribute("lista", roles.findAll());
        return "roles";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        if (nombre.isBlank() || roles.existsByNombreRol(nombre.trim())) {
            modelo.addAttribute("lista", roles.findAll());
            modelo.addAttribute("error", "Nombre vacío o repetido.");
            return "roles";
        }
        Rol rol = new Rol();
        rol.setNombreRol(nombre.trim());
        roles.save(rol);
        return "redirect:/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        try {
            roles.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            modelo.addAttribute("lista", roles.findAll());
            modelo.addAttribute("error", "No se puede borrar: ya se usó en usuarios.");
            return "roles";
        }
        return "redirect:/roles";
    }
}
