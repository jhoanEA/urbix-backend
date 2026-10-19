package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.service.AutenticacionService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebAuthController {

    private final AutenticacionService autenticacion;

    public WebAuthController(AutenticacionService autenticacion) {
        this.autenticacion = autenticacion;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String ingresar(@RequestParam String correo, @RequestParam String clave,
            HttpSession sesion, Model modelo) {
        Optional<Usuario> encontrado = autenticacion.validarCredenciales(correo, clave);
        if (encontrado.isEmpty()) {
            modelo.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }
        Usuario usuario = encontrado.get();
        if (!usuario.esAdministrador()) {
            modelo.addAttribute("error", "Esta cuenta no tiene acceso al portal de gestión.");
            return "login";
        }
        SesionPortal.abrir(sesion, usuario);
        return "redirect:/incidencias";
    }

    @GetMapping("/logout")
    public String salir(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}