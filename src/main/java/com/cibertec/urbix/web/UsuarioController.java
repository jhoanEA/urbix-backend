package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.repository.RolRepository;
import com.cibertec.urbix.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final PasswordEncoder clave;

    public UsuarioController(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder clave) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.clave = clave;
    }

    private boolean sesion(HttpSession sesion) {
        return sesion.getAttribute("usuario") != null;
    }

    @GetMapping
    public String listar(HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        modelo.addAttribute("lista", usuarios.findAll());
        modelo.addAttribute("roles", roles.findAll());
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre, @RequestParam String correo,
            @RequestParam String password, @RequestParam Integer idRol,
            HttpSession sesion, Model modelo) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        if (nombre.isBlank() || correo.isBlank() || password.isBlank()) {
            modelo.addAttribute("lista", usuarios.findAll());
        modelo.addAttribute("roles", roles.findAll());
            modelo.addAttribute("error", "Complete todos los datos.");
            return "usuarios";
        }
        if (usuarios.existsByCorreo(correo.trim())) {
            modelo.addAttribute("lista", usuarios.findAll());
        modelo.addAttribute("roles", roles.findAll());
            modelo.addAttribute("error", "Ese correo ya existe.");
            return "usuarios";
        }
        Optional<Rol> rol = roles.findById(idRol);
        if (rol.isEmpty()) {
            modelo.addAttribute("lista", usuarios.findAll());
        modelo.addAttribute("roles", roles.findAll());
            modelo.addAttribute("error", "Elija un rol válido.");
            return "usuarios";
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.trim());
        usuario.setCorreo(correo.trim());
        usuario.setClaveHash(clave.encode(password));
        usuario.setActivo(true);
        usuario.setRol(rol.get());
        usuarios.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Integer id, HttpSession sesion) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        usuarios.findById(id).ifPresent(u -> {
            u.setActivo(false);
            usuarios.save(u);
        });
        return "redirect:/usuarios";
    }

    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Integer id, HttpSession sesion) {
        if (!sesion(sesion)) {
            return "redirect:/login";
        }
        usuarios.findById(id).ifPresent(u -> {
            u.setActivo(true);
            usuarios.save(u);
        });
        return "redirect:/usuarios";
    }
}
