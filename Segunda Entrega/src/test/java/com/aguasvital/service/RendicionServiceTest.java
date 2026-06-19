package com.aguasvital.service;

import com.aguasvital.repository.DatosSistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendicionServiceTest {
    private RendicionService rendicionService;

    @BeforeEach
    void setUp() {
        DatosSistema.reiniciarParaPruebas();
        rendicionService = new RendicionService();
    }

    @Test
    void calculaTotalDeRendicion() {
        RendicionDiaria rendicion = rendicionService.generar(LocalDate.now());

        assertEquals(10400, rendicion.getTotalGeneralIngresos(), 0.01);
    }

    @Test
    void excluyePendientes() {
        RendicionDiaria rendicion = rendicionService.generar(LocalDate.now());

        assertEquals(2, rendicion.getPendientes());
        assertEquals(10400, rendicion.getTotalGeneralIngresos(), 0.01);
    }

    @Test
    void excluyeNoEntregados() {
        RendicionDiaria rendicion = rendicionService.generar(LocalDate.now());

        assertEquals(1, rendicion.getNoEntregados());
        assertEquals(10400, rendicion.getTotalGeneralIngresos(), 0.01);
    }

    @Test
    void separaContadoYElectronico() {
        RendicionDiaria rendicion = rendicionService.generar(LocalDate.now());

        assertEquals(5400, rendicion.getTotalPagosContado(), 0.01);
        assertEquals(5000, rendicion.getTotalPagosElectronicos(), 0.01);
    }
}
