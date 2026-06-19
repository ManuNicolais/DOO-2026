package com.aguasvital.controller;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EfectuarTrasladoController {
    @FXML private TableView<Pedido> tblPedidos;
    @FXML private TableColumn<Pedido, Integer> colNumero;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colDireccion;
    @FXML private TableColumn<Pedido, String> colBarrio;
    @FXML private TableColumn<Pedido, String> colZona;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TextArea txtObservaciones;

    @FXML
    private void initialize() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("nroPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        colBarrio.setCellValueFactory(new PropertyValueFactory<>("barrioCliente"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaCliente"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNombre"));
        tblPedidos.setItems(FXCollections.observableArrayList(
                DatosSistema.getInstancia().getPedidos().stream()
                        .filter(Pedido::estaPendiente)
                        .toList()));
    }

    @FXML private void marcarEntregado() { new Alert(Alert.AlertType.INFORMATION, "Prototipo: marcar entregado.").showAndWait(); }
    @FXML private void marcarNoEntregado() { new Alert(Alert.AlertType.INFORMATION, "Prototipo: marcar no entregado.").showAndWait(); }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) tblPedidos.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }
}
