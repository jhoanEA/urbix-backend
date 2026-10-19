package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.IncidenciaResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncidenciaFiltroService {

    private final IncidenciaService incidencias;

    public IncidenciaFiltroService(IncidenciaService incidencias) {
        this.incidencias = incidencias;
    }

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> filtrar(String texto, String estado, String zona,
            String categoria, String prioridad) {
        return incidencias.listar().stream()
                .filter(incidencia -> coincideTexto(incidencia, texto))
                .filter(incidencia -> coincide(estado, incidencia.getEstado()))
                .filter(incidencia -> coincide(zona, incidencia.getZona()))
                .filter(incidencia -> coincide(categoria, incidencia.getCategoria()))
                .filter(incidencia -> coincide(prioridad, incidencia.getPrioridad()))
                .toList();
    }

    private boolean coincideTexto(IncidenciaResponse incidencia, String texto) {
        if (texto == null || texto.isBlank()) {
            return true;
        }
        String buscado = texto.toLowerCase();
        return contiene(incidencia.getCategoria(), buscado)
                || contiene(incidencia.getZona(), buscado)
                || contiene(incidencia.getDescripcion(), buscado)
                || contiene(incidencia.getEstado(), buscado)
                || contiene(incidencia.getPrioridad(), buscado);
    }

    private boolean coincide(String filtro, String valor) {
        return filtro == null || filtro.isBlank() || filtro.equals(valor);
    }

    private boolean contiene(String valor, String buscado) {
        return valor != null && valor.toLowerCase().contains(buscado);
    }
}