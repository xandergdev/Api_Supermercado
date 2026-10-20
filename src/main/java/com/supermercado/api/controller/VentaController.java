package com.supermercado.api.controller;

import com.supermercado.api.dto.VentaRequest;
import com.supermercado.api.entity.Venta;
import com.supermercado.api.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venta registrar(@Valid @RequestBody VentaRequest request) {
        return ventaService.registrar(request);
    }

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public Venta buscar(@PathVariable Long id) {
        return ventaService.buscar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        ventaService.eliminar(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Venta> porCliente(@PathVariable Long clienteId) {
        return ventaService.listarPorCliente(clienteId);
    }

    @GetMapping("/fecha")
    public List<Venta> porFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ventaService.listarPorFecha(inicio, fin);
    }
}
