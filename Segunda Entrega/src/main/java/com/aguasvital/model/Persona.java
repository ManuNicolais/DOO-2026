package com.aguasvital.model;

public abstract class Persona {
    private final String nombre;
    private final String apellido;
    private final String tipoDoc;
    private final String nroDoc;
    private final String razonSocial;

    protected Persona(String nombre, String apellido, String tipoDoc, String nroDoc, String razonSocial) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.razonSocial = razonSocial;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getNombreCompleto() {
        return (nombre + " " + apellido).trim();
    }
}
