package com.aguasvital.model;

public class DetallePedido {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnitarioHistorico;
    private final double subtotal;

    public DetallePedido(Producto producto, int cantidad) {
        this(producto, cantidad, producto.getPrecioActual());
    }

    public DetallePedido(Producto producto, int cantidad, double precioUnitarioHistorico) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.subtotal = precioUnitarioHistorico * cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getNombreProducto() {
        return producto.getNombre();
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
