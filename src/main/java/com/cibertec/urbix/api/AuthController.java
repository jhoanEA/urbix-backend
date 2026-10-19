package com.cibertec.urbix.api;

import com.cibertec.urbix.dto.LoginRequest;
import com.cibertec.urbix.dto.LoginResponse;
import com.cibertec.urbix.service.AutenticacionService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacionService autenticacion;

    public AuthController(AutenticacionService autenticacion) {
        this.autenticacion = autenticacion;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest solicitud) {
        Optional<LoginResponse> respuesta = autenticacion.ingresar(solicitud);
        if (respuesta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(respuesta.get());
    }

    @PostMapping("/salir")
    public ResponseEntity<?> salir(@RequestHeader(value = "X-Token", required = false) String token) {
        autenticacion.cerrarSesion(token);
        return ResponseEntity.ok().build();
    }
}