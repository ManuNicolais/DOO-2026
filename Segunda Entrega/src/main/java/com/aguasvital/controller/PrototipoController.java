package com.aguasvital.controller;

import com.aguasvital.model.Cargo;
import com.aguasvital.util.Navegador;
import com.aguasvital.util.SesionUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrototipoController {
    @FXML private Label lblTitulo;
    @FXML private Button btnModificarPrecio;
    @FXML private ComboBox<String> cboTipoDocumento;
    @FXML private ComboBox<String> cboZona;
    @FXML private ComboBox<String> cboBarrio;

    @FXML
    private void initialize() {
        if (btnModificarPrecio != null
                && SesionUsuario.getUsuarioActivo() != null
                && SesionUsuario.getUsuarioActivo().getCargo() == Cargo.PRESIDENTE) {
            btnModificarPrecio.setDisable(true);
        }

        if (cboTipoDocumento != null) {
            cboTipoDocumento.setItems(FXCollections.observableArrayList("DNI", "CUIT"));
        }
        if (cboZona != null) {
            cboZona.setItems(FXCollections.observableArrayList("Norte", "Sur", "Este", "Oeste"));
        }
        if (cboBarrio != null) {
            cboBarrio.setItems(FXCollections.observableArrayList("Alberdi", "Centro", "Alta Cordoba", "Nueva Cordoba"));
        }
    }

    @FXML
    private void mostrarPrototipo() {
        String titulo = lblTitulo == null ? "Pantalla" : lblTitulo.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText("Prototipo navegable incluido para la segunda entrega.");
        alert.showAndWait();
    }

    @FXML private void buscar() { mostrarPrototipo(); }
    @FXML private void guardar() { mostrarPrototipo(); }
    @FXML private void limpiar() { mostrarPrototipo(); }
    @FXML private void confirmar() { mostrarPrototipo(); }
    @FXML private void actualizar() { mostrarPrototipo(); }
    @FXML private void reasignar() { mostrarPrototipo(); }
    @FXML private void verDetalle() { mostrarPrototipo(); }
    @FXML private void marcarEntregado() { mostrarPrototipo(); }
    @FXML private void marcarNoEntregado() { mostrarPrototipo(); }
    @FXML private void modificarPrecio() { mostrarPrototipo(); }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }
}
