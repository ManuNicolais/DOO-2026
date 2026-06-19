package com.aguasvital.model;

import java.time.LocalDate;

public class Precio {
    private double valor;
    private final LocalDate fechaDesde;
    private LocalDate fechaHasta;

    public Precio(double valor, LocalDate fechaDesde, LocalDate fechaHasta) {
        this.valor = valor;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void modificarPrecio(double nuevoValor) {
        if (nuevoValor <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        this.valor = nuevoValor;
    }

    public boolean estaVigente(LocalDate fecha) {
        boolean desdeOk = !fecha.isBefore(fechaDesde);
        boolean hastaOk = fechaHasta == null || !fecha.isAfter(fechaHasta);
        return desdeOk && hastaOk;
    }

    public void cerrarVigencia(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
