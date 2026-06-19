package com.aguasvital.repository;

import com.aguasvital.model.Pedido;

import java.util.List;
import java.util.Optional;

public class PedidoRepository {
    private final DatosSistema datosSistema;

    public PedidoRepository() {
        this(DatosSistema.getInstancia());
    }

    public PedidoRepository(DatosSistema datosSistema) {
        this.datosSistema = datosSistema;
    }

    public Optional<Pedido> buscarPorNumero(int nroPedido) {
        return datosSistema.buscarPedido(nroPedido);
    }

    public List<Pedido> listarTodos() {
        return datosSistema.getPedidos();
    }

    public void guardar(Pedido pedido) {
        datosSistema.persistirPedido(pedido);
    }

    public void insertar(Pedido pedido) {
        datosSistema.insertarPedido(pedido);
    }
}
