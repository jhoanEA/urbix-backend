package com.cibertec.urbix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Email @Size(max = 100) String correo,
        @Size(max = 40) String clave,
        @NotNull Integer idRol) {

    public boolean cambiaClave() {
        return clave != null && !clave.isBlank();
    }

    public boolean claveValida() {
        return cambiaClave() && clave.length() >= 4;
    }
}