package com.cibertec.urbix.repository;

import com.cibertec.urbix.model.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombreRol(String nombreRol);

    boolean existsByNombreRol(String nombreRol);

    List<Rol> findByActivoTrueOrderByNombreRolAsc();
}