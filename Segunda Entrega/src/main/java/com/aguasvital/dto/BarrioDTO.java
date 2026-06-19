package com.aguasvital.dto;

public class BarrioDTO {
    private String idBarrio;
    private String nombre;
    private String idZona;

    public BarrioDTO() {}

    public BarrioDTO(String idBarrio, String nombre, String idZona) {
        this.idBarrio = idBarrio;
        this.nombre = nombre;
        this.idZona = idZona;
    }

    public String getIdBarrio() { return idBarrio; }
    public void setIdBarrio(String idBarrio) { this.idBarrio = idBarrio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIdZona() { return idZona; }
    public void setIdZona(String idZona) { this.idZona = idZona; }
}
