package com.cibertec.urbix.repository;

import com.cibertec.urbix.model.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombreCategoria(String nombreCategoria);

    boolean existsByNombreCategoria(String nombreCategoria);
}
