package com.cibertec.urbix.api;

import com.cibertec.urbix.dto.CatalogosResponse;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.service.AutenticacionService;
import com.cibertec.urbix.service.CatalogoService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CatalogoController {

    private final AutenticacionService autenticacion;
    private final CatalogoService catalogos;

    public CatalogoController(AutenticacionService autenticacion, CatalogoService catalogos) {
        this.autenticacion = autenticacion;
        this.catalogos = catalogos;
    }

    @GetMapping("/catalogos")
    public ResponseEntity<?> catalogos(@RequestHeader(value = "X-Token", required = false) String token) {
        Optional<Usuario> autor = autenticacion.porToken(token);
        if (autor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CatalogosResponse respuesta = catalogos.listar();
        return ResponseEntity.ok(respuesta);
    }
}