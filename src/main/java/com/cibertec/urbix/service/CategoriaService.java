package com.cibertec.urbix.service;

import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categorias;

    public CategoriaService(CategoriaRepository categorias) {
        this.categorias = categorias;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return categorias.findAll();
    }

    @Transactional(readOnly = true)
    public List<Categoria> activos() {
        return categorias.findByActivoTrueOrderByNombreCategoriaAsc();
    }

    @Transactional
    public void registrar(String nombre) {
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty()) {
            throw new DatosInvalidosException("El nombre de la categoría no puede quedar vacío.");
        }
        if (categorias.existsByNombreCategoria(limpio)) {
            throw new DatosInvalidosException("Esa categoría ya existe.");
        }
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria(limpio);
        categoria.setActivo(true);
        categorias.save(categoria);
    }

    @Transactional
    public void cambiarActivo(Integer id, boolean activo) {
        Categoria categoria = categorias.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La categoría no existe."));
        categoria.setActivo(activo);
        categorias.save(categoria);
    }

    @Transactional
    public void eliminar(Integer id) {
        Categoria categoria = categorias.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La categoría no existe."));
        categorias.delete(categoria);
    }
}