package com.aguasvital.state;

import com.aguasvital.model.Pedido;

public class EstadoEntregado implements EstadoPedido {
    @Override
    public void marcarEntregado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue entregado y no puede modificarse.");
    }

    @Override
    public void marcarNoEntregado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue entregado y no puede modificarse.");
    }

    @Override
    public String obtenerNombre() {
        return "ENTREGADO";
    }
}
