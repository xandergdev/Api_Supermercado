package com.supermercado.api.service;

import com.supermercado.api.dto.ClienteRequest;
import com.supermercado.api.entity.Cliente;
import com.supermercado.api.exception.ConflictoException;
import com.supermercado.api.exception.RecursoNoEncontradoException;
import com.supermercado.api.exception.SolicitudInvalidaException;
import com.supermercado.api.repository.ClienteRepository;
import com.supermercado.api.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public ClienteService(ClienteRepository clienteRepository, VentaRepository ventaRepository) {
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id " + id));
    }

    public Cliente buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con documento " + documento));
    }

    public Cliente crear(ClienteRequest request) {
        if (clienteRepository.existsByDocumento(request.documento())) {
            throw new SolicitudInvalidaException("Ya existe un cliente con el documento " + request.documento());
        }
        Cliente cliente = new Cliente();
        copiar(cliente, request);
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscar(id);
        if (clienteRepository.existsByDocumentoAndIdNot(request.documento(), id)) {
            throw new SolicitudInvalidaException("Ya existe otro cliente con el documento " + request.documento());
        }
        copiar(cliente, request);
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        Cliente cliente = buscar(id);
        if (ventaRepository.existsByClienteId(id)) {
            throw new ConflictoException("No se puede eliminar el cliente porque tiene ventas registradas");
        }
        clienteRepository.delete(cliente);
    }

    private void copiar(Cliente cliente, ClienteRequest request) {
        cliente.setNombre(request.nombre());
        cliente.setDocumento(request.documento());
        cliente.setTelefono(request.telefono());
    }
}
