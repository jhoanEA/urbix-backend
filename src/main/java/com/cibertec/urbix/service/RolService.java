package com.cibertec.urbix.service;

import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.model.Rol;
import com.cibertec.urbix.repository.RolRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    private final RolRepository roles;

    public RolService(RolRepository roles) {
        this.roles = roles;
    }

    @Transactional(readOnly = true)
    public List<Rol> listar() {
        return roles.findAll();
    }

    @Transactional(readOnly = true)
    public List<Rol> activos() {
        return roles.findByActivoTrueOrderByNombreRolAsc();
    }

    @Transactional
    public void registrar(String nombre) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty()) {
            throw new DatosInvalidosException("El nombre del rol no puede quedar vacío.");
        }
        if (roles.existsByNombreRol(limpio)) {
            throw new DatosInvalidosException("Ese rol ya existe.");
        }
        Rol rol = new Rol();
        rol.setNombreRol(limpio);
        rol.setActivo(true);
        roles.save(rol);
    }

    @Transactional
    public void cambiarActivo(Integer id, boolean activo) {
        Rol rol = roles.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El rol no existe."));
        rol.setActivo(activo);
        roles.save(rol);
    }

    @Transactional
    public void eliminar(Integer id) {
        Rol rol = roles.findById(id)
                .orElseThrow(() -> new NoEncontradoException("El rol no existe."));
        roles.delete(rol);
    }
}