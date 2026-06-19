package com.aguasvital.dto;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {
    private int nroPedido;
    private LocalDate fechaEmision;
    private LocalDate fechaEntrega;
    private String estado;
    private double total;
    private String nroDocCliente;
    private String nombreCliente;
    private String direccionCliente;
    private String barrioCliente;
    private String zonaCliente;
    private String metodoPago;
    private boolean pagoProcesado;
    private String observaciones;
    private List<DetallePedidoDTO> detalles;

    public PedidoDTO() {}

    public int getNroPedido() { return nroPedido; }
    public void setNroPedido(int nroPedido) { this.nroPedido = nroPedido; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getNroDocCliente() { return nroDocCliente; }
    public void setNroDocCliente(String nroDocCliente) { this.nroDocCliente = nroDocCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }
    public String getBarrioCliente() { return barrioCliente; }
    public void setBarrioCliente(String barrioCliente) { this.barrioCliente = barrioCliente; }
    public String getZonaCliente() { return zonaCliente; }
    public void setZonaCliente(String zonaCliente) { this.zonaCliente = zonaCliente; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public boolean isPagoProcesado() { return pagoProcesado; }
    public void setPagoProcesado(boolean pagoProcesado) { this.pagoProcesado = pagoProcesado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
}
