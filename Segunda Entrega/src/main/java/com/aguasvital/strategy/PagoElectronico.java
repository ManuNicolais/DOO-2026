package com.aguasvital.strategy;

public class PagoElectronico implements MetodoPago {
    private final String nroOperacion;
    private final String entidadPago;

    public PagoElectronico(String nroOperacion, String entidadPago) {
        this.nroOperacion = nroOperacion;
        this.entidadPago = entidadPago;
    }

    public String getNroOperacion() {
        return nroOperacion;
    }

    public String getEntidadPago() {
        return entidadPago;
    }

    @Override
    public boolean procesarPago(double total) {
        return total > 0 && esTextoValido(entidadPago) && esTextoValido(nroOperacion);
    }

    @Override
    public String obtenerDescripcion() {
        return "Pago electronico - " + entidadPago;
    }

    private boolean esTextoValido(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
}
