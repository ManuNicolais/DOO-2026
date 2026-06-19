package com.aguasvital.controller;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ConsultarPedidosController {
    @FXML private TextField txtNumeroPedido;
    @FXML private TextField txtCliente;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableView<Pedido> tblPedidos;
    @FXML private TableColumn<Pedido, Integer> colNumero;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colFechaEmision;
    @FXML private TableColumn<Pedido, String> colFechaEntrega;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, String> colTotal;

    @FXML
    private void initialize() {
        cmbEstado.setItems(FXCollections.observableArrayList("PENDIENTE", "ENTREGADO", "NO_ENTREGADO"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("nroPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colFechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNombre"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalFormateado"));
        cargarTodos();
    }

    @FXML
    private void buscar() {
        var pedidos = DatosSistema.getInstancia().getPedidos().stream()
                .filter(p -> txtNumeroPedido.getText().isBlank()
                        || String.valueOf(p.getNroPedido()).contains(txtNumeroPedido.getText().trim()))
                .filter(p -> txtCliente.getText().isBlank()
                        || p.getNombreCliente().toLowerCase().contains(txtCliente.getText().trim().toLowerCase()))
                .filter(p -> dpFecha.getValue() == null || p.getFechaEntrega().equals(dpFecha.getValue()))
                .filter(p -> cmbEstado.getValue() == null || p.getEstadoNombre().equals(cmbEstado.getValue()))
                .toList();
        tblPedidos.setItems(FXCollections.observableArrayList(pedidos));
    }

    @FXML
    private void limpiar() {
        txtNumeroPedido.clear();
        txtCliente.clear();
        dpFecha.setValue(null);
        cmbEstado.setValue(null);
        cargarTodos();
    }

    @FXML
    private void verDetalle() {
        Pedido pedido = tblPedidos.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle del pedido");
        alert.setHeaderText(null);
        alert.setContentText(pedido == null ? "Seleccione un pedido." :
                "Pedido " + pedido.getNroPedido() + "\nCliente: " + pedido.getNombreCliente()
                        + "\nEstado: " + pedido.getEstadoNombre()
                        + "\nTotal: " + pedido.getTotalFormateado());
        alert.showAndWait();
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) tblPedidos.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void cargarTodos() {
        tblPedidos.setItems(FXCollections.observableArrayList(DatosSistema.getInstancia().getPedidos()));
    }
}
