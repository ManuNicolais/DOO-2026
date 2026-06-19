package com.aguasvital.strategy;

public class PagoContado implements MetodoPago {
    private final double montoRecibido;

    public PagoContado(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    public double getMontoRecibido() {
        return montoRecibido;
    }

    @Override
    public boolean procesarPago(double total) {
        return montoRecibido >= total;
    }

    @Override
    public String obtenerDescripcion() {
        return "Pago contado";
    }
}
