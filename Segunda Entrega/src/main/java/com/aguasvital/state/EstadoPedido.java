package com.aguasvital.state;

import com.aguasvital.model.Pedido;

public interface EstadoPedido {
    void marcarEntregado(Pedido pedido);

    void marcarNoEntregado(Pedido pedido);

    String obtenerNombre();
}
