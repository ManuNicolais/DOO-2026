package com.aguasvital.dto;

public class ClienteDTO {
    private String nroDoc;
    private String nombre;
    private String apellido;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String idBarrio;

    public ClienteDTO() {}

    public ClienteDTO(String nroDoc, String nombre, String apellido, String razonSocial,
                      String direccion, String telefono, String idBarrio) {
        this.nroDoc = nroDoc;
        this.nombre = nombre;
        this.apellido = apellido;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.idBarrio = idBarrio;
    }

    public String getNroDoc() { return nroDoc; }
    public void setNroDoc(String nroDoc) { this.nroDoc = nroDoc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getIdBarrio() { return idBarrio; }
    public void setIdBarrio(String idBarrio) { this.idBarrio = idBarrio; }
}
