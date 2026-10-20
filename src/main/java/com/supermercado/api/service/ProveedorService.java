package com.supermercado.api.service;

import com.supermercado.api.dto.ProveedorRequest;
import com.supermercado.api.entity.Proveedor;
import com.supermercado.api.exception.ConflictoException;
import com.supermercado.api.exception.RecursoNoEncontradoException;
import com.supermercado.api.repository.ProductoRepository;
import com.supermercado.api.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    public ProveedorService(ProveedorRepository proveedorRepository, ProductoRepository productoRepository) {
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor buscar(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con id " + id));
    }

    public Proveedor crear(ProveedorRequest request) {
        Proveedor proveedor = new Proveedor();
        copiar(proveedor, request);
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = buscar(id);
        copiar(proveedor, request);
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Long id) {
        Proveedor proveedor = buscar(id);
        if (productoRepository.existsByProveedorId(id)) {
            throw new ConflictoException("No se puede eliminar el proveedor porque tiene productos asociados");
        }
        proveedorRepository.delete(proveedor);
    }

    private void copiar(Proveedor proveedor, ProveedorRequest request) {
        proveedor.setNombre(request.nombre());
        proveedor.setTelefono(request.telefono());
        proveedor.setCorreo(request.correo());
    }
}
