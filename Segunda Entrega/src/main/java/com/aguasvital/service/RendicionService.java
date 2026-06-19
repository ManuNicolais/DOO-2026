package com.aguasvital.service;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.PedidoRepository;
import com.aguasvital.strategy.PagoContado;
import com.aguasvital.strategy.PagoElectronico;

import java.time.LocalDate;
import java.util.List;

public class RendicionService {
    private final PedidoRepository pedidoRepository;

    public RendicionService() {
        this(new PedidoRepository());
    }

    public RendicionService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public RendicionDiaria generar(LocalDate fecha) {
        List<Pedido> pedidosFecha = pedidoRepository.listarTodos().stream()
                .filter(pedido -> pedido.getFechaEntrega().equals(fecha))
                .toList();

        long entregados = pedidosFecha.stream().filter(Pedido::estaEntregado).count();
        long noEntregados = pedidosFecha.stream().filter(Pedido::estaNoEntregado).count();
        long pendientes = pedidosFecha.stream().filter(Pedido::estaPendiente).count();

        double contado = pedidosFecha.stream()
                .filter(this::generaIngreso)
                .filter(p -> p.getMetodoPago() instanceof PagoContado)
                .mapToDouble(Pedido::getTotal)
                .sum();

        double electronico = pedidosFecha.stream()
                .filter(this::generaIngreso)
                .filter(p -> p.getMetodoPago() instanceof PagoElectronico)
                .mapToDouble(Pedido::getTotal)
                .sum();

        return new RendicionDiaria(fecha, pedidosFecha, entregados, noEntregados, pendientes,
                contado + electronico, contado, electronico);
    }

    private boolean generaIngreso(Pedido pedido) {
        return pedido.estaEntregado()
                && pedido.getMetodoPago() != null
                && pedido.isPagoProcesado();
    }
}
