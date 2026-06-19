package com.aguasvital.controller;

import com.aguasvital.model.Pedido;
import com.aguasvital.service.RendicionDiaria;
import com.aguasvital.service.RendicionService;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;

public class GenerarRendicionController {
    @FXML private DatePicker dpFecha;
    @FXML private TableView<Pedido> tblPedidos;
    @FXML private TableColumn<Pedido, Integer> colNroPedido;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colBarrio;
    @FXML private TableColumn<Pedido, String> colZona;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, String> colMetodoPago;
    @FXML private TableColumn<Pedido, String> colTotal;
    @FXML private Label lblEntregados;
    @FXML private Label lblNoEntregados;
    @FXML private Label lblPendientes;
    @FXML private Label lblTotalGeneral;
    @FXML private Label lblTotalContado;
    @FXML private Label lblTotalElectronico;

    private final RendicionService rendicionService = new RendicionService();

    @FXML
    private void initialize() {
        dpFecha.setValue(LocalDate.now());
        colNroPedido.setCellValueFactory(new PropertyValueFactory<>("nroPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colBarrio.setCellValueFactory(new PropertyValueFactory<>("barrioCliente"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaCliente"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoNombre"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPagoDescripcion"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalFormateado"));
        generarRendicion();
    }

    @FXML
    private void generarRendicion() {
        LocalDate fecha = dpFecha.getValue() == null ? LocalDate.now() : dpFecha.getValue();
        RendicionDiaria rendicion = rendicionService.generar(fecha);
        tblPedidos.setItems(FXCollections.observableArrayList(rendicion.getPedidos()));
        lblEntregados.setText(String.valueOf(rendicion.getEntregados()));
        lblNoEntregados.setText(String.valueOf(rendicion.getNoEntregados()));
        lblPendientes.setText(String.valueOf(rendicion.getPendientes()));
        lblTotalGeneral.setText(formato(rendicion.getTotalGeneralIngresos()));
        lblTotalContado.setText(formato(rendicion.getTotalPagosContado()));
        lblTotalElectronico.setText(formato(rendicion.getTotalPagosElectronicos()));
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) dpFecha.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private String formato(double valor) {
        return "$" + String.format("%.2f", valor);
    }
}
