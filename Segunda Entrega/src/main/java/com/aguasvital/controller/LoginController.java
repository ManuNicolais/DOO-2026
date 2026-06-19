package com.aguasvital.controller;

import com.aguasvital.repository.DatosSistema;
import com.aguasvital.repository.UsuarioSistema;
import com.aguasvital.util.Navegador;
import com.aguasvital.util.SesionUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();
        Optional<UsuarioSistema> usuarioValido = DatosSistema.getInstancia().validarUsuario(usuario, password);
        if (usuarioValido.isEmpty()) {
            lblError.setText("Usuario o contrasena incorrectos.");
            return;
        }
        SesionUsuario.iniciar(usuarioValido.get());
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }
}
