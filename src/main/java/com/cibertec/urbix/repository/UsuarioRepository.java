package com.cibertec.urbix.repository;

import com.cibertec.urbix.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByToken(String token);

    boolean existsByCorreo(String correo);
}