package com.cibertec.urbix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ZonaRequest(
        @Size(max = 100) String distrito,
        @NotBlank @Size(max = 100) String sector) {
}