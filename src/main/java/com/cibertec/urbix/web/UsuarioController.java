package com.cibertec.urbix.web;

import com.cibertec.urbix.dto.UsuarioRequest;
import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarios;

    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("lista", usuarios.listar());
        modelo.addAttribute("roles", usuarios.rolesActivos());
        modelo.addAttribute("editando", null);
        return "usuarios";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model modelo) {
        Usuario usuario = usuarios.obtener(id);
        modelo.addAttribute("lista", usuarios.listar());
        modelo.addAttribute("roles", usuarios.rolesActivos());
        modelo.addAttribute("editando", usuario);
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid UsuarioRequest solicitud, Model modelo) {
        return persistir(solicitud, null, modelo);
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Integer id, @Valid UsuarioRequest solicitud, Model modelo) {
        return persistir(solicitud, id, modelo);
    }

    @PostMapping("/{id}/estado")
    public String estado(@PathVariable Integer id, @RequestParam boolean activo) {
        usuarios.cambiarActivo(id, activo);
        return "redirect:/usuarios";
    }

    private String persistir(UsuarioRequest solicitud, Integer id, Model modelo) {
        try {
            if (id == null) {
                usuarios.registrar(solicitud);
            } else {
                usuarios.actualizar(id, solicitud);
            }
        } catch (DatosInvalidosException e) {
            modelo.addAttribute("lista", usuarios.listar());
            modelo.addAttribute("roles", usuarios.rolesActivos());
            modelo.addAttribute("editando", id == null ? null : usuarios.obtener(id));
            modelo.addAttribute("error", e.getMessage());
            return "usuarios";
        }
        return "redirect:/usuarios";
    }
}