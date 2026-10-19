package com.cibertec.urbix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadoRequest(
        @NotBlank @Size(max = 30) String estado) {
}