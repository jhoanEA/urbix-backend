package com.cibertec.urbix.repository;

import com.cibertec.urbix.model.Zona;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZonaRepository extends JpaRepository<Zona, Integer> {

    Optional<Zona> findByNombreSector(String nombreSector);

    boolean existsByNombreSector(String nombreSector);

    List<Zona> findByActivoTrueOrderByNombreSectorAsc();

    long countByActivoFalse();
}