package com.supermercado.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaRequest(
        @NotNull(message = "El cliente es obligatorio") Long clienteId,
        @NotEmpty(message = "La venta debe tener al menos un detalle") List<@Valid DetalleVentaRequest> detalles) {
}
