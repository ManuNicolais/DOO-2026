package com.aguasvital.strategy;

public interface MetodoPago {
    boolean procesarPago(double total);

    String obtenerDescripcion();
}
