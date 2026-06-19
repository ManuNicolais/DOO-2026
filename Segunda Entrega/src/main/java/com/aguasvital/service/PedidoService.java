package com.aguasvital.service;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.PedidoRepository;
import com.aguasvital.strategy.MetodoPago;

import java.util.Optional;

public class PedidoService {
    public static final String ENTREGADO = "ENTREGADO";
    public static final String NO_ENTREGADO = "NO_ENTREGADO";

    private final PedidoRepository pedidoRepository;

    public PedidoService() {
        this(new PedidoRepository());
    }

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Optional<Pedido> buscarPedido(int nroPedido) {
        return pedidoRepository.buscarPorNumero(nroPedido);
    }

    public void registrarPedido(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("El pedido debe tener un cliente asignado.");
        }
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto.");
        }
        pedidoRepository.insertar(pedido);
    }

    public void actualizarPedido(int nroPedido, String estadoDestino, MetodoPago metodoPago, String observaciones) {
        Pedido pedido = pedidoRepository.buscarPorNumero(nroPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido inexistente."));
        if (!pedido.estaPendiente()) {
            throw new IllegalStateException("El pedido ya fue procesado y no puede actualizarse.");
        }
        if (ENTREGADO.equals(estadoDestino)) {
            if (metodoPago == null) {
                throw new IllegalArgumentException("Debe seleccionar un metodo de pago.");
            }
            pedido.seleccionarMetodoPago(metodoPago);
            if (!pedido.procesarPago()) {
                throw new IllegalArgumentException("El pago no fue aprobado.");
            }
            pedido.registrarObservaciones(observaciones);
            pedido.marcarEntregado();
            pedidoRepository.guardar(pedido);
            return;
        }
        if (NO_ENTREGADO.equals(estadoDestino)) {
            pedido.registrarObservaciones(observaciones);
            pedido.marcarNoEntregado();
            pedidoRepository.guardar(pedido);
            return;
        }
        throw new IllegalArgumentException("Debe seleccionar un estado valido.");
    }
}
