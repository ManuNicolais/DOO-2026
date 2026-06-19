package com.aguasvital.dto;

public class DetallePedidoDTO {
    private int id;
    private int nroPedido;
    private String codProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetallePedidoDTO() {}

    public DetallePedidoDTO(int nroPedido, String codProducto, String nombreProducto,
                            int cantidad, double precioUnitario, double subtotal) {
        this.nroPedido = nroPedido;
        this.codProducto = codProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNroPedido() { return nroPedido; }
    public void setNroPedido(int nroPedido) { this.nroPedido = nroPedido; }
    public String getCodProducto() { return codProducto; }
    public void setCodProducto(String codProducto) { this.codProducto = codProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
