package com.aguasvital.model;

/**
 * Opcional - para futura implementacion.
 * Soporta a Factura cuando se implemente la generacion de comprobantes.
 */
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
