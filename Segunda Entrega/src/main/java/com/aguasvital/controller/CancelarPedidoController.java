package com.aguasvital.controller;

import com.aguasvital.model.Pedido;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelarPedidoController {
    @FXML private TextField txtNumeroPedido;
    @FXML private TextField txtCliente;
    @FXML private TextField txtFecha;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotal;
    @FXML private TextArea txtMotivo;

    private Pedido pedidoActual;

    @FXML
    private void buscar() {
        String texto = txtNumeroPedido.getText().trim();
        if (texto.isEmpty()) {
            mostrarAlerta("Ingrese un numero de pedido.");
            return;
        }
        try {
            int nro = Integer.parseInt(texto);
            pedidoActual = DatosSistema.getInstancia().buscarPedido(nro).orElse(null);
            if (pedidoActual == null) {
                mostrarAlerta("Pedido no encontrado.");
                limpiarDetalle();
                return;
            }
            txtCliente.setText(pedidoActual.getNombreCliente());
            txtFecha.setText(pedidoActual.getFechaEntrega().toString());
            txtEstado.setText(pedidoActual.getEstadoNombre());
            txtTotal.setText(pedidoActual.getTotalFormateado());
        } catch (NumberFormatException e) {
            mostrarAlerta("Ingrese un numero valido.");
        }
    }

    @FXML
    private void confirmar() {
        if (pedidoActual == null) {
            mostrarAlerta("Busque un pedido primero.");
            return;
        }
        if (!pedidoActual.estaPendiente()) {
            mostrarAlerta("Solo se pueden cancelar pedidos pendientes.");
            return;
        }
        mostrarAlerta("Funcionalidad en desarrollo: cancelacion de pedido #" + pedidoActual.getNroPedido());
    }

    @FXML
    private void limpiar() {
        pedidoActual = null;
        txtNumeroPedido.clear();
        limpiarDetalle();
        txtMotivo.clear();
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) txtNumeroPedido.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void limpiarDetalle() {
        txtCliente.clear();
        txtFecha.clear();
        txtEstado.clear();
        txtTotal.clear();
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
}
