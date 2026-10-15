package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebAuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder clave;

    public WebAuthController(UsuarioRepository usuarios, PasswordEncoder clave) {
        this.usuarios = usuarios;
        this.clave = clave;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String ingresar(@RequestParam String correo, @RequestParam String password,
            HttpSession sesion, Model modelo) {
        Optional<Usuario> encontrado = usuarios.findByCorreo(correo.trim());
        if (encontrado.isEmpty() || !encontrado.get().getActivo()
                || !clave.matches(password, encontrado.get().getClaveHash())) {
            modelo.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }
        sesion.setAttribute("usuario", encontrado.get().getNombre());
        return "redirect:/incidencias";
    }

    @GetMapping("/logout")
    public String salir(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}
