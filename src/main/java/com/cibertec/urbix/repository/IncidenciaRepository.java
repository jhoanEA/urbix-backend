package com.cibertec.urbix.repository;

import com.cibertec.urbix.model.Incidencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {

    Optional<Incidencia> findByClientUuid(String clientUuid);

    boolean existsByClientUuid(String clientUuid);

    List<Incidencia> findAllByOrderByIdIncidenciaDesc();
}
