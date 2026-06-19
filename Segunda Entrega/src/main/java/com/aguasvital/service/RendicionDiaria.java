package com.aguasvital.service;

import com.aguasvital.model.Pedido;

import java.time.LocalDate;
import java.util.List;

public class RendicionDiaria {
    private final LocalDate fecha;
    private final List<Pedido> pedidos;
    private final long entregados;
    private final long noEntregados;
    private final long pendientes;
    private final double totalGeneralIngresos;
    private final double totalPagosContado;
    private final double totalPagosElectronicos;

    public RendicionDiaria(LocalDate fecha, List<Pedido> pedidos, long entregados, long noEntregados,
                           long pendientes, double totalGeneralIngresos, double totalPagosContado,
                           double totalPagosElectronicos) {
        this.fecha = fecha;
        this.pedidos = pedidos;
        this.entregados = entregados;
        this.noEntregados = noEntregados;
        this.pendientes = pendientes;
        this.totalGeneralIngresos = totalGeneralIngresos;
        this.totalPagosContado = totalPagosContado;
        this.totalPagosElectronicos = totalPagosElectronicos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public long getEntregados() {
        return entregados;
    }

    public long getNoEntregados() {
        return noEntregados;
    }

    public long getPendientes() {
        return pendientes;
    }

    public double getTotalGeneralIngresos() {
        return totalGeneralIngresos;
    }

    public double getTotalPagosContado() {
        return totalPagosContado;
    }

    public double getTotalPagosElectronicos() {
        return totalPagosElectronicos;
    }
}
