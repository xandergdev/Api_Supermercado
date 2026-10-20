package com.supermercado.api.controller;

import com.supermercado.api.dto.ProveedorRequest;
import com.supermercado.api.entity.Proveedor;
import com.supermercado.api.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proveedor crear(@Valid @RequestBody ProveedorRequest request) {
        return proveedorService.crear(request);
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public Proveedor buscar(@PathVariable Long id) {
        return proveedorService.buscar(id);
    }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        return proveedorService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
    }
}
