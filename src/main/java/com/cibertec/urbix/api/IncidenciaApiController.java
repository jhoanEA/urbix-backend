package com.cibertec.urbix.api;

import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.model.Incidencia;
import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.IncidenciaRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaApiController {

    private final IncidenciaRepository incidencias;
    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;

    private static final Set<String> ESTADOS = Set.of("Reportada", "En Proceso", "Solucionada");

    public IncidenciaApiController(IncidenciaRepository incidencias,
            CategoriaRepository categorias, ZonaRepository zonas) {
        this.incidencias = incidencias;
        this.categorias = categorias;
        this.zonas = zonas;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Map<String, Object>> respuesta = new ArrayList<>();
        for (Incidencia i : incidencias.findAllByOrderByIdIncidenciaDesc()) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("client_uuid", i.getClientUuid());
            fila.put("categoria", i.getCategoria() != null ? i.getCategoria().getNombreCategoria() : "");
            fila.put("zona", i.getZona() != null ? i.getZona().getNombreSector() : "");
            fila.put("descripcion", i.getDescripcion());
            fila.put("estado", i.getEstado());
            respuesta.add(fila);
        }
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<?> recibir(@RequestBody Map<String, Object> datos) {
        String uuid = String.valueOf(datos.getOrDefault("client_uuid", "")).trim();
        if (uuid.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (incidencias.existsByClientUuid(uuid)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Incidencia incidencia = new Incidencia();
        incidencia.setClientUuid(uuid);
        incidencia.setDescripcion(String.valueOf(datos.getOrDefault("descripcion", "")));
        incidencia.setEstado(valido(String.valueOf(datos.getOrDefault("estado", "Reportada"))));
        incidencia.setCategoria(buscarCategoria(String.valueOf(datos.getOrDefault("categoria", ""))));
        incidencia.setZona(buscarZona(String.valueOf(datos.getOrDefault("zona", ""))));
        incidencia.setLatitud(numero(datos.get("latitud")));
        incidencia.setLongitud(numero(datos.get("longitud")));
        Object foto = datos.get("foto");
        incidencia.setFotoUrl(foto == null ? null : String.valueOf(foto));
        incidencias.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{uuid}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable String uuid,
            @RequestBody Map<String, String> datos) {
        Optional<Incidencia> encontrada = incidencias.findByClientUuid(uuid);
        if (encontrada.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String estado = datos.getOrDefault("estado", "").trim();
        if (!ESTADOS.contains(estado)) {
            return ResponseEntity.badRequest().build();
        }
        Incidencia incidencia = encontrada.get();
        incidencia.setEstado(estado);
        incidencias.save(incidencia);
        return ResponseEntity.ok().build();
    }

    private String valido(String estado) {
        return ESTADOS.contains(estado) ? estado : "Reportada";
    }

    private Categoria buscarCategoria(String nombre) {
        if (nombre.isBlank()) {
            return null;
        }
        Optional<Categoria> encontrada = categorias.findByNombreCategoria(nombre);
        if (encontrada.isPresent()) {
            return encontrada.get();
        }
        Categoria nueva = new Categoria();
        nueva.setNombreCategoria(nombre);
        return categorias.save(nueva);
    }

    private Zona buscarZona(String nombre) {
        if (nombre.isBlank()) {
            return null;
        }
        Optional<Zona> encontrada = zonas.findByNombreSector(nombre);
        if (encontrada.isPresent()) {
            return encontrada.get();
        }
        Zona nueva = new Zona();
        nueva.setNombreSector(nombre);
        return zonas.save(nueva);
    }

    private Double numero(Object valor) {
        if (valor == null) {
            return null;
        }
        try {
            return Double.valueOf(String.valueOf(valor));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
