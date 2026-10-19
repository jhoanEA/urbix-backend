package com.cibertec.urbix.service;

import com.cibertec.urbix.dto.ZonaRequest;
import com.cibertec.urbix.exception.DatosInvalidosException;
import com.cibertec.urbix.exception.NoEncontradoException;
import com.cibertec.urbix.model.Zona;
import com.cibertec.urbix.repository.ZonaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZonaService {

    private final ZonaRepository zonas;

    public ZonaService(ZonaRepository zonas) {
        this.zonas = zonas;
    }

    @Transactional(readOnly = true)
    public List<Zona> listar() {
        return zonas.findAll();
    }

    @Transactional(readOnly = true)
    public List<Zona> activos() {
        return zonas.findByActivoTrueOrderByNombreSectorAsc();
    }

    @Transactional
    public void registrar(ZonaRequest solicitud) {
        String sector = solicitud.sector() == null ? "" : solicitud.sector().trim();
        if (sector.isEmpty()) {
            throw new DatosInvalidosException("El nombre del sector no puede quedar vacío.");
        }
        if (zonas.existsByNombreSector(sector)) {
            throw new DatosInvalidosException("Ese sector ya existe.");
        }
        Zona zona = new Zona();
        zona.setDistrito(solicitud.distrito() == null ? null : solicitud.distrito().trim());
        zona.setNombreSector(sector);
        zona.setActivo(true);
        zonas.save(zona);
    }

    @Transactional
    public void cambiarActivo(Integer id, boolean activo) {
        Zona zona = zonas.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La zona no existe."));
        zona.setActivo(activo);
        zonas.save(zona);
    }

    @Transactional
    public void eliminar(Integer id) {
        Zona zona = zonas.findById(id)
                .orElseThrow(() -> new NoEncontradoException("La zona no existe."));
        zonas.delete(zona);
    }
}