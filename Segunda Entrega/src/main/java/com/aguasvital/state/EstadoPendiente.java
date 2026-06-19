package com.aguasvital.state;

import com.aguasvital.model.Pedido;

public class EstadoPendiente implements EstadoPedido {
    @Override
    public void marcarEntregado(Pedido pedido) {
        pedido.cambiarEstado(new EstadoEntregado());
    }

    @Override
    public void marcarNoEntregado(Pedido pedido) {
        pedido.cambiarEstado(new EstadoNoEntregado());
    }

    @Override
    public String obtenerNombre() {
        return "PENDIENTE";
    }
}
