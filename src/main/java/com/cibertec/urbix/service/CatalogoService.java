package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.CatalogosResponse;
import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoService {

    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;

    public CatalogoService(CategoriaRepository categorias, ZonaRepository zonas) {
        this.categorias = categorias;
        this.zonas = zonas;
    }

    @Transactional(readOnly = true)
    public CatalogosResponse listar() {
        List<String> listaCategorias = categorias.findByActivoTrueOrderByNombreCategoriaAsc().stream()
                .map(categoria -> categoria.getNombreCategoria())
                .toList();
        List<String> listaZonas = zonas.findByActivoTrueOrderByNombreSectorAsc().stream()
                .map(zona -> zona.getNombreSector())
                .toList();
        return new CatalogosResponse(listaCategorias, listaZonas);
    }
}