package com.cibertec.urbix.web;

import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.exception.SinPermisoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.cibertec.urbix.web")
public class ManejadorErroresPortal {

    @ExceptionHandler(SinPermisoException.class)
    public ModelAndView sinPermiso(SinPermisoException excepcion) {
        return error(HttpStatus.FORBIDDEN, "Acceso restringido", excepcion.getMessage());
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ModelAndView noEncontrado(NoEncontradoException excepcion) {
        return error(HttpStatus.NOT_FOUND, "Registro no encontrado", excepcion.getMessage());
    }

    private ModelAndView error(HttpStatus estado, String titulo, String detalle) {
        ModelAndView vista = new ModelAndView("error");
        vista.addObject("titulo", titulo);
        vista.addObject("detalle", detalle);
        vista.setStatus(estado);
        return vista;
    }
}