package com.aguasvital.service;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.strategy.PagoContado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        DatosSistema.reiniciarParaPruebas();
        pedidoService = new PedidoService();
    }

    @Test
    void actualizaPendienteAEntregado() {
        pedidoService.actualizarPedido(1001, PedidoService.ENTREGADO, new PagoContado(10000), "Entregado");

        Pedido pedido = pedidoService.buscarPedido(1001).orElseThrow();
        assertTrue(pedido.estaEntregado());
        assertEquals("ENTREGADO", pedido.getEstadoNombre());
        assertTrue(pedido.isPagoProcesado());
    }

    @Test
    void actualizaPendienteANoEntregado() {
        pedidoService.actualizarPedido(1002, PedidoService.NO_ENTREGADO, null, "Cliente ausente");

        Pedido pedido = pedidoService.buscarPedido(1002).orElseThrow();
        assertTrue(pedido.estaNoEntregado());
        assertEquals("NO_ENTREGADO", pedido.getEstadoNombre());
        assertFalse(pedido.isPagoProcesado());
    }

    @Test
    void rechazaSegundaActualizacion() {
        pedidoService.actualizarPedido(1001, PedidoService.ENTREGADO, new PagoContado(10000), "Entregado");

        assertThrows(IllegalStateException.class,
                () -> pedidoService.actualizarPedido(1001, PedidoService.NO_ENTREGADO, null, "Reintento"));
    }
}
