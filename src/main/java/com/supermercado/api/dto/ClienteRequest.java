package com.supermercado.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El documento es obligatorio") String documento,
        String telefono) {
}
