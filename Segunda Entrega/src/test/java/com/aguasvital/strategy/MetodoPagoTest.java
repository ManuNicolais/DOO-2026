package com.aguasvital.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetodoPagoTest {
    @Test
    void pagoContadoValido() {
        assertTrue(new PagoContado(1000).procesarPago(900));
    }

    @Test
    void pagoContadoInsuficiente() {
        assertFalse(new PagoContado(500).procesarPago(900));
    }

    @Test
    void pagoElectronicoValido() {
        assertTrue(new PagoElectronico("OP-1", "Mercado Pago").procesarPago(1000));
    }

    @Test
    void pagoElectronicoSinNumeroOperacion() {
        assertFalse(new PagoElectronico("", "Mercado Pago").procesarPago(1000));
    }
}
