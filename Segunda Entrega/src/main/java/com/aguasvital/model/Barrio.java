package com.aguasvital.model;

public class Barrio {
    private final String idBarrio;
    private final String nombre;
    private final Zona zona;

    public Barrio(String idBarrio, String nombre, Zona zona) {
        this.idBarrio = idBarrio;
        this.nombre = nombre;
        this.zona = zona;
    }

    public String getIdBarrio() {
        return idBarrio;
    }

    public String getNombre() {
        return nombre;
    }

    public Zona getZona() {
        return zona;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
