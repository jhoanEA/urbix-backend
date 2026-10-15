package com.cibertec.urbix.api;

import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.repository.UsuarioRepository;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder clave;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder clave) {
        this.usuarios = usuarios;
        this.clave = clave;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        String correo = datos.getOrDefault("correo", "").trim();
        String password = datos.getOrDefault("clave", "");
        Optional<Usuario> encontrado = usuarios.findByCorreo(correo);
        if (encontrado.isEmpty() || !encontrado.get().getActivo()
                || !clave.matches(password, encontrado.get().getClaveHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Map.of("rol", encontrado.get().getRol().getNombreRol()));
    }
}
