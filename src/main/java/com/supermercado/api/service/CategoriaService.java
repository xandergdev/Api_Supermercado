package com.supermercado.api.service;

import com.supermercado.api.dto.CategoriaRequest;
import com.supermercado.api.entity.Categoria;
import com.supermercado.api.exception.ConflictoException;
import com.supermercado.api.exception.RecursoNoEncontradoException;
import com.supermercado.api.repository.CategoriaRepository;
import com.supermercado.api.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id " + id));
    }

    public Categoria crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscar(id);
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = buscar(id);
        if (productoRepository.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
    }
}
