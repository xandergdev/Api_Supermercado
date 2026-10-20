package com.supermercado.api.service;

import com.supermercado.api.dto.DetalleVentaRequest;
import com.supermercado.api.dto.VentaRequest;
import com.supermercado.api.entity.Cliente;
import com.supermercado.api.entity.DetalleVenta;
import com.supermercado.api.entity.Producto;
import com.supermercado.api.entity.Venta;
import com.supermercado.api.exception.RecursoNoEncontradoException;
import com.supermercado.api.exception.SolicitudInvalidaException;
import com.supermercado.api.repository.ProductoRepository;
import com.supermercado.api.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteService clienteService;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, ClienteService clienteService) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteService = clienteService;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta buscar(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con id " + id));
    }

    public List<Venta> listarPorCliente(Long clienteId) {
        clienteService.buscar(clienteId);
        return ventaRepository.findByClienteId(clienteId);
    }

    public List<Venta> listarPorFecha(LocalDate inicio, LocalDate fin) {
        if (fin.isBefore(inicio)) {
            throw new SolicitudInvalidaException("La fecha final no puede ser anterior a la inicial");
        }
        return ventaRepository.findByFechaBetween(inicio.atStartOfDay(), fin.atTime(LocalTime.MAX));
    }

    @Transactional
    public Venta registrar(VentaRequest request) {
        Cliente cliente = clienteService.buscar(request.clienteId());

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVentaRequest item : request.detalles()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id " + item.productoId()));

            if (producto.getStock() < item.cantidad()) {
                throw new SolicitudInvalidaException("Stock insuficiente para '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock() + ", solicitado: " + item.cantidad());
            }

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.cantidad()));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            venta.agregarDetalle(detalle);

            total = total.add(subtotal);
        }

        venta.setTotal(total);
        return ventaRepository.save(venta);
    }

    @Transactional
    public void eliminar(Long id) {
        Venta venta = buscar(id);
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        ventaRepository.delete(venta);
    }
}
