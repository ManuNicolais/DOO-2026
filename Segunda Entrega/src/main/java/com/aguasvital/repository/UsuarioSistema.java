package com.aguasvital.repository;

import com.aguasvital.model.Cargo;

public class UsuarioSistema {
    private final String usuario;
    private final Cargo cargo;

    public UsuarioSistema(String usuario, Cargo cargo) {
        this.usuario = usuario;
        this.cargo = cargo;
    }

    public String getUsuario() {
        return usuario;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
