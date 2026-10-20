package com.supermercado.api.service;

import com.supermercado.api.dto.ProductoRequest;
import com.supermercado.api.entity.Producto;
import com.supermercado.api.exception.ConflictoException;
import com.supermercado.api.exception.RecursoNoEncontradoException;
import com.supermercado.api.exception.SolicitudInvalidaException;
import com.supermercado.api.repository.DetalleVentaRepository;
import com.supermercado.api.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public ProductoService(ProductoRepository productoRepository, DetalleVentaRepository detalleVentaRepository, CategoriaService categoriaService, ProveedorService proveedorService) {
        this.productoRepository = productoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id " + id));
    }

    public Producto crear(ProductoRequest request) {
        if (productoRepository.existsByCodigoBarras(request.codigoBarras())) {
            throw new SolicitudInvalidaException("Ya existe un producto con el código de barras " + request.codigoBarras());
        }
        Producto producto = new Producto();
        copiar(producto, request);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, ProductoRequest request) {
        Producto producto = buscar(id);
        if (productoRepository.existsByCodigoBarrasAndIdNot(request.codigoBarras(), id)) {
            throw new SolicitudInvalidaException("Ya existe otro producto con el código de barras " + request.codigoBarras());
        }
        copiar(producto, request);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = buscar(id);
        if (detalleVentaRepository.existsByProductoId(id)) {
            throw new ConflictoException("No se puede eliminar el producto porque está en ventas registradas");
        }
        productoRepository.delete(producto);
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        categoriaService.buscar(categoriaId);
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> stockBajo(Integer limite) {
        return productoRepository.findByStockLessThanEqual(limite);
    }

    private void copiar(Producto producto, ProductoRequest request) {
        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setCodigoBarras(request.codigoBarras());
        producto.setCategoria(categoriaService.buscar(request.categoriaId()));
        producto.setProveedor(proveedorService.buscar(request.proveedorId()));
    }
}
