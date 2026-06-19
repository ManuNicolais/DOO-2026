package com.aguasvital.controller;

import com.aguasvital.model.Cargo;
import com.aguasvital.model.Empleado;
import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RegistrarPendientesController {
    @FXML private TableView<Pedido> tblPendientes;
    @FXML private TableColumn<Pedido, Integer> colNumero;
    @FXML private TableColumn<Pedido, String> colFecha;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colDireccion;
    @FXML private TableColumn<Pedido, String> colBarrio;
    @FXML private TableColumn<Pedido, String> colZona;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, String> colDistribuidor;
    @FXML private ComboBox<String> cmbDistribuidor;

    @FXML
    private void initialize() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("nroPedido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        colBarrio.setCellValueFactory(new PropertyValueFactory<>("barrioCliente"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaCliente"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNombre"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<>("distribuidorAsignado"));
        cmbDistribuidor.setItems(FXCollections.observableArrayList(
                DatosSistema.getInstancia().getEmpleados().stream()
                        .filter(e -> e.getCargo() == Cargo.DISTRIBUIDOR)
                        .map(e -> e.getNombre() + " " + e.getApellido())
                        .toList()));
        cargar();
    }

    @FXML private void reasignar() { new Alert(Alert.AlertType.INFORMATION, "Prototipo: pedido reasignado.").showAndWait(); }
    @FXML private void actualizar() { cargar(); }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) tblPendientes.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void cargar() {
        tblPendientes.setItems(FXCollections.observableArrayList(
                DatosSistema.getInstancia().getPedidos().stream()
                        .filter(Pedido::estaPendiente)
                        .toList()));
    }
}
