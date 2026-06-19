package com.aguasvital.model;

public class Cliente extends Persona {
    private final String direccion;
    private final String telefono;
    private final Barrio barrio;

    public Cliente(String nroDoc, String nombre, String apellido, String razonSocial,
                   String direccion, String telefono, Barrio barrio) {
        super(nombre, apellido, "DNI", nroDoc, razonSocial);
        this.direccion = direccion;
        this.telefono = telefono;
        this.barrio = barrio;
    }

    public String getDni() {
        return getNroDoc();
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public String getNombreBarrio() {
        return barrio.getNombre();
    }

    public String getNombreZona() {
        return barrio.getZona().getNombre();
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}
