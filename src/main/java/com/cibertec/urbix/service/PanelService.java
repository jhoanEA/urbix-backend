package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.IncidenciaResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanelService {

    private final IncidenciaService incidencias;

    public PanelService(IncidenciaService incidencias) {
        this.incidencias = incidencias;
    }

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> ultimas(int cantidad) {
        List<IncidenciaResponse> lista = incidencias.listar();
        return lista.size() <= cantidad ? lista : lista.subList(0, cantidad);
    }

    @Transactional(readOnly = true)
    public long contar(String estado) {
        return incidencias.listar().stream()
                .filter(incidencia -> estado.equals(incidencia.getEstado()))
                .count();
    }

    @Transactional(readOnly = true)
    public long total() {
        return incidencias.listar().size();
    }
}