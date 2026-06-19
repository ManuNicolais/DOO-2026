package com.aguasvital.state;

import com.aguasvital.model.DetallePedido;
import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.strategy.PagoContado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPedidoTest {
    @BeforeEach
    void setUp() {
        DatosSistema.reiniciarParaPruebas();
    }

    @Test
    void pedidoNuevoComienzaPendiente() {
        Pedido pedido = nuevoPedido();

        assertTrue(pedido.estaPendiente());
        assertEquals("PENDIENTE", pedido.getEstadoNombre());
    }

    @Test
    void estadoPendientePermitePasarAEntregado() {
        Pedido pedido = nuevoPedido();

        pedido.marcarEntregado();

        assertTrue(pedido.estaEntregado());
        assertEquals("ENTREGADO", pedido.getEstadoNombre());
    }

    @Test
    void estadoPendientePermitePasarANoEntregado() {
        Pedido pedido = nuevoPedido();

        pedido.marcarNoEntregado();

        assertTrue(pedido.estaNoEntregado());
        assertEquals("NO_ENTREGADO", pedido.getEstadoNombre());
    }

    @Test
    void estadoEntregadoRechazaVolverAMarcarEntregado() {
        Pedido pedido = nuevoPedido();
        pedido.marcarEntregado();

        assertThrows(IllegalStateException.class, pedido::marcarEntregado);
    }

    @Test
    void estadoEntregadoRechazaMarcarNoEntregado() {
        Pedido pedido = nuevoPedido();
        pedido.marcarEntregado();

        assertThrows(IllegalStateException.class, pedido::marcarNoEntregado);
    }

    @Test
    void estadoNoEntregadoRechazaMarcarEntregado() {
        Pedido pedido = nuevoPedido();
        pedido.marcarNoEntregado();

        assertThrows(IllegalStateException.class, pedido::marcarEntregado);
    }

    @Test
    void estadoNoEntregadoRechazaSegundaModificacion() {
        Pedido pedido = nuevoPedido();
        pedido.marcarNoEntregado();

        assertThrows(IllegalStateException.class, pedido::marcarNoEntregado);
    }

    @Test
    void strategySigueFuncionandoConState() {
        Pedido pedido = nuevoPedido();
        pedido.seleccionarMetodoPago(new PagoContado(10000));

        assertTrue(pedido.procesarPago());
        pedido.marcarEntregado();

        assertTrue(pedido.estaEntregado());
        assertTrue(pedido.isPagoProcesado());
    }

    private Pedido nuevoPedido() {
        var datos = DatosSistema.getInstancia();
        return new Pedido(2001, LocalDate.now(), LocalDate.now(), datos.getClientes().get(0),
                List.of(new DetallePedido(datos.getProductos().get(0), 1)));
    }
}
