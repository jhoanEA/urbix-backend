package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.LoginRequest;
import com.cibertec.urbix.dto.LoginResponse;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.repository.UsuarioRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacionService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder clave;

    public AutenticacionService(UsuarioRepository usuarios, PasswordEncoder clave) {
        this.usuarios = usuarios;
        this.clave = clave;
    }

    @Transactional
    public Optional<LoginResponse> ingresar(LoginRequest solicitud) {
        return validarCredenciales(solicitud.correo(), solicitud.clave())
                .map(this::emitirToken);
    }

    @Transactional
    public void cerrarSesion(String token) {
        porToken(token).ifPresent(usuario -> {
            usuario.setToken(null);
            usuarios.save(usuario);
        });
    }

    public Optional<Usuario> porToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return usuarios.findByToken(token.trim()).filter(Usuario::getActivo);
    }

    public Optional<Usuario> validarCredenciales(String correo, String clavePlana) {
        Optional<Usuario> encontrado = usuarios.findByCorreo(correo.trim());
        if (encontrado.isEmpty() || !encontrado.get().getActivo()
                || !clave.matches(clavePlana, encontrado.get().getClaveHash())) {
            return Optional.empty();
        }
        return encontrado;
    }

    private LoginResponse emitirToken(Usuario usuario) {
        usuario.setToken(UUID.randomUUID().toString());
        usuarios.save(usuario);
        return new LoginResponse(
                usuario.getToken(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombreRol());
    }
}