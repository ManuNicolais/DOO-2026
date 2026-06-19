package com.aguasvital.model;

public class DetalleFactura {
    private final DetallePedido detallePedido;

    public DetalleFactura(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
    }

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public double getSubtotal() {
        return detallePedido.getSubtotal();
    }
}
