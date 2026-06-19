package com.aguasvital.controller;

import com.aguasvital.model.Barrio;
import com.aguasvital.model.Zona;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrarClienteController {
    @FXML private ComboBox<String> cboTipoDocumento;
    @FXML private TextField txtNroDoc;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private ComboBox<Zona> cboZona;
    @FXML private ComboBox<Barrio> cboBarrio;

    private final DatosSistema datos = DatosSistema.getInstancia();

    @FXML
    private void initialize() {
        cboTipoDocumento.setItems(FXCollections.observableArrayList("DNI", "CUIT", "Pasaporte"));
        cboTipoDocumento.setValue("DNI");

        cboZona.setItems(FXCollections.observableArrayList(datos.getZonas()));

        cboZona.valueProperty().addListener((obs, oldVal, nuevaZona) -> {
            if (nuevaZona != null) {
                cboBarrio.setItems(FXCollections.observableArrayList(
                        datos.getBarrios().stream()
                                .filter(b -> b.getZona().getIdZona().equals(nuevaZona.getIdZona()))
                                .toList()));
                cboBarrio.setValue(null);
            } else {
                cboBarrio.getItems().clear();
            }
        });
    }

    @FXML
    private void guardar() {
        if (txtNroDoc.getText().isBlank() || txtNombre.getText().isBlank() || txtApellido.getText().isBlank()) {
            mostrarAlerta("Complete Nro documento, Nombre y Apellido.");
            return;
        }
        if (cboZona.getValue() == null || cboBarrio.getValue() == null) {
            mostrarAlerta("Seleccione zona y barrio.");
            return;
        }
        mostrarAlerta("Funcionalidad en desarrollo: registro de cliente " + txtNombre.getText() + " " + txtApellido.getText());
    }

    @FXML
    private void limpiar() {
        txtNroDoc.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtRazonSocial.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        cboZona.setValue(null);
        cboBarrio.getItems().clear();
        cboBarrio.setValue(null);
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) txtNroDoc.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
}
