package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.UsuarioRequest;
import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.repository.RolRepository;
import com.cibertec.urbix.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final PasswordEncoder clave;

    public UsuarioService(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder clave) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.clave = clave;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarios.findAll();
    }

    @Transactional(readOnly = true)
    public List<Rol> rolesActivos() {
        return roles.findByActivoTrueOrderByNombreRolAsc();
    }

    @Transactional(readOnly = true)
    public Usuario obtener(Integer id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El usuario no existe."));
    }

    @Transactional
    public void registrar(UsuarioRequest solicitud) {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);
        preparar(usuario, solicitud, true);
        usuarios.save(usuario);
    }

    @Transactional
    public void actualizar(Integer id, UsuarioRequest solicitud) {
        Usuario usuario = obtener(id);
        preparar(usuario, solicitud, false);
        usuarios.save(usuario);
    }

    @Transactional
    public void cambiarActivo(Integer id, boolean activo) {
        Usuario usuario = obtener(id);
        usuario.setActivo(activo);
        if (!activo) {
            usuario.setToken(null);
        }
        usuarios.save(usuario);
    }

    private void preparar(Usuario usuario, UsuarioRequest solicitud, boolean esNuevo) {
        if (esNuevo && !solicitud.claveValida()) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (!esNuevo && solicitud.cambiaClave() && !solicitud.claveValida()) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 4 caracteres.");
        }
        String correo = solicitud.correo().trim();
        if (!correo.equals(usuario.getCorreo()) && usuarios.existsByCorreo(correo)) {
            throw new DatosInvalidosException("Ese correo ya está registrado.");
        }
        Rol rol = roles.findById(solicitud.idRol())
                .orElseThrow(() -> new DatosInvalidosException("Elija un rol válido."));
        usuario.setNombre(solicitud.nombre().trim());
        usuario.setCorreo(correo);
        usuario.setRol(rol);
        if (solicitud.cambiaClave()) {
            usuario.setClaveHash(clave.encode(solicitud.clave()));
        }
    }
}