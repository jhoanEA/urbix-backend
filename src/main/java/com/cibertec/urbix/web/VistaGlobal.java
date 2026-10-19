package com.cibertec.urbix.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.cibertec.urbix.web")
public class VistaGlobal {

    @ModelAttribute("nombreUsuario")
    public String nombreUsuario(HttpSession sesion) {
        return SesionPortal.nombre(sesion);
    }

    @ModelAttribute("rolUsuario")
    public String rolUsuario(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute(SesionPortal.ROL) == null) {
            return "";
        }
        return sesion.getAttribute(SesionPortal.ROL).toString();
    }
}