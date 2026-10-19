package com.cibertec.urbix.api;

import com.cibertec.urbix.dto.EstadoRequest;
import com.cibertec.urbix.dto.IncidenciaRequest;
import com.cibertec.urbix.dto.IncidenciaResponse;
import com.cibertec.urbix.media.TipoImagen;
import com.cibertec.urbix.model.Incidencia;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.service.AutenticacionService;
import com.cibertec.urbix.service.IncidenciaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaApiController {

    private final AutenticacionService autenticacion;
    private final IncidenciaService incidencias;

    public IncidenciaApiController(AutenticacionService autenticacion,
            IncidenciaService incidencias) {
        this.autenticacion = autenticacion;
        this.incidencias = incidencias;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader(value = "X-Token", required = false) String token) {
        Optional<Usuario> autor = autenticacion.porToken(token);
        if (autor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<IncidenciaResponse> lista = incidencias.listar();
        return ResponseEntity.ok(lista);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> recibir(@RequestHeader(value = "X-Token", required = false) String token,
            @Valid @RequestPart("datos") IncidenciaRequest datos,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {
        Optional<Usuario> autor = autenticacion.porToken(token);
        if (autor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        incidencias.registrar(datos, foto, autor.get());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{uuid}/estado")
    public ResponseEntity<?> cambiarEstado(
            @RequestHeader(value = "X-Token", required = false) String token,
            @PathVariable String uuid,
            @Valid @RequestBody EstadoRequest solicitud) {
        if (autenticacion.porToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        incidencias.cambiarEstado(uuid, solicitud);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uuid}/foto")
    public ResponseEntity<byte[]> foto(
            @RequestHeader(value = "X-Token", required = false) String token,
            @PathVariable String uuid) {
        if (autenticacion.porToken(token).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Incidencia incidencia = incidencias.conFoto(uuid);
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(TipoImagen.de(incidencia.getFoto()));
        cabeceras.setContentLength(incidencia.getFoto().length);
        return new ResponseEntity<>(incidencia.getFoto(), cabeceras, HttpStatus.OK);
    }
}