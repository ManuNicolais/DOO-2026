package com.aguasvital.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Producto {
    private final String codigo;
    private final String nombre;
    private final Envase envase;
    private final List<Precio> precios = new ArrayList<>();

    public Producto(String codigo, String nombre, Envase envase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.envase = envase;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Envase getEnvase() {
        return envase;
    }

    public List<Precio> getPrecios() {
        return precios;
    }

    public void agregarPrecio(Precio precio) {
        precios.add(precio);
    }

    public double getPrecioActual() {
        LocalDate hoy = LocalDate.now();
        return precios.stream()
                .filter(precio -> precio.estaVigente(hoy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Producto sin precio vigente: " + nombre))
                .getValor();
    }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%.2f", getPrecioActual()) + ")";
    }
}
