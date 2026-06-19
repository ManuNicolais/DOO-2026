package com.aguasvital.model;

public enum Cargo {
    DISTRIBUIDOR("Distribuidor"),
    OPERADORA("Operadora"),
    ENCARGADO_ADMINISTRATIVO("Encargado Administrativo"),
    PRESIDENTE("Presidente");

    private final String descripcion;

    Cargo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
