package com.cibertec.urbix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Email @Size(max = 100) String correo,
        @NotBlank @Size(min = 4, max = 40) String clave) {
}