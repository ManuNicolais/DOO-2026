package com.aguasvital.model;

public class Zona {
    private final String idZona;
    private final String nombre;

    public Zona(String idZona, String nombre) {
        this.idZona = idZona;
        this.nombre = nombre;
    }

    public String getIdZona() {
        return idZona;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
