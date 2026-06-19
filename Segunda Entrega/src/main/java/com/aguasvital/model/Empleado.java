package com.aguasvital.model;

public class Empleado extends Persona {
    private final Cargo cargo;
    private final int capacidadMaxima;

    public Empleado(String nombre, String apellido, String tipoDoc, String nroDoc, String razonSocial,
                    Cargo cargo, int capacidadMaxima) {
        super(nombre, apellido, tipoDoc, nroDoc, razonSocial);
        this.cargo = cargo;
        this.capacidadMaxima = capacidadMaxima;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}
