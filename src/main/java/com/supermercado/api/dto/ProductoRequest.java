package com.supermercado.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser mayor a 0") BigDecimal precio,
        @NotNull(message = "El stock es obligatorio") @Min(value = 0, message = "El stock no puede ser negativo") Integer stock,
        @NotBlank(message = "El código de barras es obligatorio") String codigoBarras,
        @NotNull(message = "La categoría es obligatoria") Long categoriaId,
        @NotNull(message = "El proveedor es obligatorio") Long proveedorId) {
}
