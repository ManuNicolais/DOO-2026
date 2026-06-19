package com.aguasvital.dto;

public class ProductoDTO {
    private String codigo;
    private String nombre;
    private String tipoEnvase;
    private int capacidad;
    private double precioActual;

    public ProductoDTO() {}

    public ProductoDTO(String codigo, String nombre, String tipoEnvase, int capacidad, double precioActual) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoEnvase = tipoEnvase;
        this.capacidad = capacidad;
        this.precioActual = precioActual;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoEnvase() { return tipoEnvase; }
    public void setTipoEnvase(String tipoEnvase) { this.tipoEnvase = tipoEnvase; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public double getPrecioActual() { return precioActual; }
    public void setPrecioActual(double precioActual) { this.precioActual = precioActual; }
}
