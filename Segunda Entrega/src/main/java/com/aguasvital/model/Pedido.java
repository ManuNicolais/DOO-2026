package com.aguasvital.model;

import com.aguasvital.state.EstadoEntregado;
import com.aguasvital.state.EstadoNoEntregado;
import com.aguasvital.state.EstadoPedido;
import com.aguasvital.state.EstadoPendiente;
import com.aguasvital.strategy.MetodoPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final int nroPedido;
    private final LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private EstadoPedido estadoActual;
    private final double total;
    private final List<DetallePedido> detalles;
    private final Cliente cliente;
    private MetodoPago metodoPago;
    private String observaciones;
    private boolean pagoProcesado;

    public Pedido(int nroPedido, LocalDate fechaEmision, LocalDate fechaEntrega,
                  Cliente cliente, List<DetallePedido> detalles) {
        this.nroPedido = nroPedido;
        this.fechaEmision = fechaEmision;
        this.fechaEntrega = fechaEntrega;
        this.estadoActual = new EstadoPendiente();
        this.cliente = cliente;
        this.detalles = new ArrayList<>(detalles);
        this.observaciones = "";
        this.total = calcularTotal();
    }

    public double calcularTotal() {
        return detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    public void marcarEntregado() {
        estadoActual.marcarEntregado(this);
    }

    public void marcarNoEntregado() {
        estadoActual.marcarNoEntregado(this);
    }

    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }
        this.estadoActual = nuevoEstado;
    }

    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }

    public String getEstadoNombre() {
        return estadoActual.obtenerNombre();
    }

    public boolean estaPendiente() {
        return estadoActual instanceof EstadoPendiente;
    }

    public boolean estaEntregado() {
        return estadoActual instanceof EstadoEntregado;
    }

    public boolean estaNoEntregado() {
        return estadoActual instanceof EstadoNoEntregado;
    }

    public void registrarObservaciones(String observaciones) {
        this.observaciones = observaciones == null ? "" : observaciones;
    }

    public void seleccionarMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean procesarPago() {
        if (metodoPago == null) {
            pagoProcesado = false;
            return false;
        }
        pagoProcesado = metodoPago.procesarPago(total);
        return pagoProcesado;
    }

    public int getNroPedido() {
        return nroPedido;
    }

    public int getId() {
        return nroPedido;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getTotal() {
        return total;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isPagoProcesado() {
        return pagoProcesado;
    }

    public void marcarPagoProcesado(boolean pagoProcesado) {
        this.pagoProcesado = pagoProcesado;
    }

    public String getNombreCliente() {
        return cliente.getNombreCompleto();
    }

    public String getDireccionCliente() {
        return cliente.getDireccion();
    }

    public String getBarrioCliente() {
        return cliente.getNombreBarrio();
    }

    public String getZonaCliente() {
        return cliente.getNombreZona();
    }

    public String getTotalFormateado() {
        return "$" + String.format("%.2f", total);
    }

    public String getEstadoDescripcion() {
        return getEstadoNombre();
    }

    public String getMetodoPagoDescripcion() {
        return metodoPago == null ? "-" : metodoPago.obtenerDescripcion();
    }

    public String getDistribuidorAsignado() {
        return "Diego Sosa";
    }
}
