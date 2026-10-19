package com.cibertec.urbix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IncidenciaRequest(
        @NotBlank @Size(max = 50) String clientUuid,
        @NotBlank @Size(max = 100) String categoria,
        @NotBlank @Size(max = 100) String zona,
        @NotBlank @Size(max = 2000) String descripcion,
        @NotBlank @Size(max = 20) String prioridad,
        @NotBlank @Size(max = 30) String estado,
        Double latitud,
        Double longitud) {
}