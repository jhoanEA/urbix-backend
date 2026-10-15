package com.cibertec.urbix.api;

import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CatalogoController {

    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;

    public CatalogoController(CategoriaRepository categorias, ZonaRepository zonas) {
        this.categorias = categorias;
        this.zonas = zonas;
    }

    @GetMapping("/catalogos")
    public ResponseEntity<?> catalogos() {
        List<String> listaCategorias = categorias.findAll().stream()
                .map(c -> c.getNombreCategoria()).toList();
        List<String> listaZonas = zonas.findAll().stream()
                .map(z -> z.getNombreSector()).toList();
        return ResponseEntity.ok(Map.of("categorias", listaCategorias, "zonas", listaZonas));
    }
}
