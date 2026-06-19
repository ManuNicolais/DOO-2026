package com.aguasvital.model;

public class Envase {
    private final String tipoEnvase;
    private final int capacidad;

    public Envase(String tipoEnvase, int capacidad) {
        this.tipoEnvase = tipoEnvase;
        this.capacidad = capacidad;
    }

    public String getTipoEnvase() {
        return tipoEnvase;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public String toString() {
        return tipoEnvase + " " + capacidad + "L";
    }
}
