package com.cibertec.urbix.web;

import com.cibertec.urbix.exception.SinPermisoException;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FiltroPortal implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws IOException {
        HttpSession sesion = request.getSession(false);
        if (!SesionPortal.haySesion(sesion)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        if (!SesionPortal.esAdministrador(sesion)) {
            throw new SinPermisoException("Solo el administrador entra al portal de gestión.");
        }
        return true;
    }
}