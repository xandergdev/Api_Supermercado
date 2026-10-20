package com.supermercado.api.controller;

import com.supermercado.api.dto.ProductoRequest;
import com.supermercado.api.entity.Producto;
import com.supermercado.api.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody ProductoRequest request) {
        return productoService.crear(request);
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return productoService.buscar(id);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> porCategoria(@PathVariable Long categoriaId) {
        return productoService.listarPorCategoria(categoriaId);
    }

    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo(@RequestParam(defaultValue = "10") Integer limite) {
        return productoService.stockBajo(limite);
    }
}
