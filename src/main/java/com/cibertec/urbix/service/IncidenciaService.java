package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.EstadoRequest;
import com.cibertec.urbix.dto.IncidenciaRequest;
import com.cibertec.urbix.dto.IncidenciaResponse;
import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.exception.YaExisteException;
import com.cibertec.urbix.model.Categoria;
import com.cibertec.urbix.model.Incidencia;
import com.cibertec.urbix.model.Usuario;
import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.CategoriaRepository;
import com.cibertec.urbix.repository.IncidenciaRepository;
import com.cibertec.urbix.repository.ZonaRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IncidenciaService {

    public static final Set<String> ESTADOS = Set.of("Reportada", "En Proceso", "Solucionada");
    public static final Set<String> PRIORIDADES = Set.of("Alta", "Media", "Baja");

    private static final long TAMANO_MAXIMO = 2L * 1024L * 1024L;

    private final IncidenciaRepository incidencias;
    private final CategoriaRepository categorias;
    private final ZonaRepository zonas;

    public IncidenciaService(IncidenciaRepository incidencias, CategoriaRepository categorias,
            ZonaRepository zonas) {
        this.incidencias = incidencias;
        this.categorias = categorias;
        this.zonas = zonas;
    }

    @Transactional
    public Incidencia registrar(IncidenciaRequest datos, MultipartFile foto, Usuario autor) {
        String uuid = datos.clientUuid().trim();
        if (incidencias.existsByClientUuid(uuid)) {
            throw new YaExisteException(uuid);
        }
        Incidencia incidencia = new Incidencia();
        incidencia.setClientUuid(uuid);
        incidencia.setDescripcion(datos.descripcion().trim());
        incidencia.setPrioridad(prioridadValida(datos.prioridad()));
        incidencia.setEstado(estadoValido(datos.estado()));
        incidencia.setCategoria(categoriaDe(datos.categoria()));
        incidencia.setZona(zonaDe(datos.zona()));
        incidencia.setLatitud(datos.latitud());
        incidencia.setLongitud(datos.longitud());
        incidencia.setUsuario(autor);
        incidencia.setFechaRegistro(LocalDateTime.now());
        if (foto != null && !foto.isEmpty()) {
            incidencia.setFotoNombre(foto.getOriginalFilename());
            incidencia.setFoto(bytesDe(foto));
        }
        incidencias.save(incidencia);
        return incidencia;
    }

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listar() {
        return IncidenciaResponse.de(incidencias.findAllByOrderByIdIncidenciaDesc());
    }

    @Transactional
    public Incidencia cambiarEstado(String uuid, EstadoRequest solicitud) {
        Incidencia incidencia = buscar(uuid);
        String estado = solicitud.estado().trim();
        if (!ESTADOS.contains(estado)) {
            throw new DatosInvalidosException("El estado no pertenece al flujo permitido.");
        }
        incidencia.setEstado(estado);
        incidencias.save(incidencia);
        return incidencia;
    }

    @Transactional
    public void actualizarSeguimiento(Integer id, String estado) {
        Incidencia incidencia = incidencias.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La incidencia no existe."));
        if (estado != null && ESTADOS.contains(estado)) {
            incidencia.setEstado(estado);
        }
        incidencias.save(incidencia);
    }

    @Transactional(readOnly = true)
    public Incidencia buscarPorCodigo(String uuid) {
        return buscar(uuid);
    }

    @Transactional(readOnly = true)
    public Incidencia conFoto(String uuid) {
        Incidencia incidencia = buscar(uuid);
        if (!incidencia.tieneFoto()) {
            throw new NoEncontradoException("La incidencia no tiene foto.");
        }
        return incidencia;
    }

    private Incidencia buscar(String uuid) {
        return incidencias.findByClientUuid(uuid)
                .orElseThrow(() -> new NoEncontradoException("La incidencia no existe en el servidor."));
    }

    private String prioridadValida(String prioridad) {
        return PRIORIDADES.contains(prioridad) ? prioridad : "Baja";
    }

    private String estadoValido(String estado) {
        return ESTADOS.contains(estado) ? estado : "Reportada";
    }

    private Categoria categoriaDe(String nombre) {
        return categorias.findByNombreCategoria(nombre.trim())
                .orElseThrow(() -> new DatosInvalidosException(
                        "La categoría enviada no está en el catálogo del servidor."));
    }

    private Zona zonaDe(String nombre) {
        return zonas.findByNombreSector(nombre.trim())
                .orElseThrow(() -> new DatosInvalidosException(
                        "La zona enviada no está en el catálogo del servidor."));
    }

    private byte[] bytesDe(MultipartFile foto) {
        if (foto.getSize() > TAMANO_MAXIMO) {
            throw new DatosInvalidosException("La foto supera el tamaño permitido de 2 MB.");
        }
        try {
            return foto.getBytes();
        } catch (IOException e) {
            throw new DatosInvalidosException("No se pudo leer la foto enviada.");
        }
    }
}