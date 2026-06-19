package com.aguasvital.util;

import com.aguasvital.repository.UsuarioSistema;

public class SesionUsuario {
    private static UsuarioSistema usuarioActivo;

    private SesionUsuario() {
    }

    public static UsuarioSistema getUsuarioActivo() {
        return usuarioActivo;
    }

    public static void iniciar(UsuarioSistema usuario) {
        usuarioActivo = usuario;
    }

    public static void cerrar() {
        usuarioActivo = null;
    }
}
