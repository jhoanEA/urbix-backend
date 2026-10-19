package com.cibertec.urbix.api;

import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.exception.SinPermisoException;
import com.cibertec.urbix.exception.YaExisteException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.cibertec.urbix.api")
public class ManejadorErroresApi {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validacion(MethodArgumentNotValidException excepcion) {
        String mensaje = excepcion.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Los datos enviados no son válidos.");
        return ResponseEntity.badRequest().body(Map.of("mensaje", mensaje));
    }

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<?> datosInvalidos(DatosInvalidosException excepcion) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", excepcion.getMessage()));
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<?> noEncontrado(NoEncontradoException excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", excepcion.getMessage()));
    }

    @ExceptionHandler(YaExisteException.class)
    public ResponseEntity<?> yaExiste(YaExisteException excepcion) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", excepcion.getMessage()));
    }

    @ExceptionHandler(SinPermisoException.class)
    public ResponseEntity<?> sinPermiso(SinPermisoException excepcion) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("mensaje", excepcion.getMessage()));
    }
}