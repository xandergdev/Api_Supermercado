package com.supermercado.api.repository;

import com.supermercado.api.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    boolean existsByProductoId(Long productoId);
}
