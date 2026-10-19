package com.cibertec.urbix.web;

import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.model.Usuario;
import jakarta.servlet.http.HttpSession;

public final class SesionPortal {

    public static final String USUARIO = "usuario";
    public static final String ROL = "rol";
    public static final String CORREO = "correo";

    private SesionPortal() {
    }

    public static void abrir(HttpSession sesion, Usuario usuario) {
        sesion.setAttribute(USUARIO, usuario.getNombre());
        sesion.setAttribute(CORREO, usuario.getCorreo());
        sesion.setAttribute(ROL, usuario.getRol().getNombreRol());
    }

    public static boolean haySesion(HttpSession sesion) {
        return sesion != null && sesion.getAttribute(USUARIO) != null;
    }

    public static boolean esAdministrador(HttpSession sesion) {
        return haySesion(sesion) && Rol.ADMINISTRADOR.equals(sesion.getAttribute(ROL));
    }

    public static String nombre(HttpSession sesion) {
        Object valor = sesion.getAttribute(USUARIO);
        return valor == null ? "" : valor.toString();
    }
}