package com.aguasvital.state;

import com.aguasvital.model.Pedido;

public class EstadoNoEntregado implements EstadoPedido {
    @Override
    public void marcarEntregado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue marcado como no entregado y no puede modificarse.");
    }

    @Override
    public void marcarNoEntregado(Pedido pedido) {
        throw new IllegalStateException("El pedido ya fue marcado como no entregado y no puede modificarse.");
    }

    @Override
    public String obtenerNombre() {
        return "NO_ENTREGADO";
    }
}
