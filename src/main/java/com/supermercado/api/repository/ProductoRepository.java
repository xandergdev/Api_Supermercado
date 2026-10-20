package com.supermercado.api.repository;

import com.supermercado.api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByProveedorId(Long proveedorId);

    boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarrasAndIdNot(String codigoBarras, Long id);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByStockLessThanEqual(Integer limite);
}
