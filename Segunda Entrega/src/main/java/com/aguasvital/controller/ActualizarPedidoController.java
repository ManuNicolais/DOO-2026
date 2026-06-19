package com.aguasvital.controller;

import com.aguasvital.model.DetallePedido;
import com.aguasvital.model.Pedido;
import com.aguasvital.service.PedidoService;
import com.aguasvital.strategy.MetodoPago;
import com.aguasvital.strategy.PagoContado;
import com.aguasvital.strategy.PagoElectronico;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ActualizarPedidoController {
    @FXML private TextField txtNumeroPedido;
    @FXML private TextField txtNumero;
    @FXML private TextField txtCliente;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBarrio;
    @FXML private TextField txtZona;
    @FXML private TextField txtFechaEmision;
    @FXML private TextField txtFechaEntrega;
    @FXML private TextField txtTotal;
    @FXML private TextField txtEstadoActual;
    @FXML private TableView<DetallePedido> tblDetalles;
    @FXML private TableColumn<DetallePedido, String> colProducto;
    @FXML private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML private TableColumn<DetallePedido, Double> colPrecio;
    @FXML private TableColumn<DetallePedido, Double> colSubtotal;
    @FXML private ToggleGroup estadoGroup;
    @FXML private ToggleGroup pagoGroup;
    @FXML private HBox boxEstado;
    @FXML private RadioButton rbEntregado;
    @FXML private RadioButton rbNoEntregado;
    @FXML private RadioButton rbContado;
    @FXML private RadioButton rbElectronico;
    @FXML private HBox boxMetodoPago;
    @FXML private HBox boxContado;
    @FXML private HBox boxElectronico;
    @FXML private TextField txtMontoRecibido;
    @FXML private TextField txtEntidadPago;
    @FXML private TextField txtNroOperacion;
    @FXML private TextArea txtObservaciones;
    @FXML private Label lblMensaje;
    @FXML private Button btnConfirmar;

    private final PedidoService pedidoService = new PedidoService();
    private Pedido pedidoActual;

    @FXML
    private void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitarioHistorico"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        rbEntregado.selectedProperty().addListener((obs, oldValue, newValue) -> actualizarVisibilidadPago());
        rbNoEntregado.selectedProperty().addListener((obs, oldValue, newValue) -> actualizarVisibilidadPago());
        rbContado.selectedProperty().addListener((obs, oldValue, newValue) -> actualizarVisibilidadPago());
        rbElectronico.selectedProperty().addListener((obs, oldValue, newValue) -> actualizarVisibilidadPago());
        limpiarFormulario();
        mostrarOk("Pedidos disponibles para probar: 1001 y 1002.");
    }

    @FXML
    private void buscarPedido() {
        limpiarMensaje();
        Integer numero = leerNumeroPedido();
        if (numero == null) {
            return;
        }
        pedidoActual = pedidoService.buscarPedido(numero).orElse(null);
        if (pedidoActual == null) {
            mostrarError("Pedido inexistente.");
            limpiarDetalle();
            deshabilitarActualizacion();
            return;
        }
        cargarPedido(pedidoActual);
        if (!pedidoActual.estaPendiente()) {
            mostrarError("El pedido ya fue procesado y no puede actualizarse.");
            deshabilitarActualizacion();
            return;
        }
        habilitarActualizacion();
        mostrarOk("Pedido encontrado. Seleccione ENTREGADO o NO_ENTREGADO.");
    }

    @FXML
    private void confirmarActualizacion() {
        limpiarMensaje();
        if (pedidoActual == null) {
            mostrarError("Primero debe buscar un pedido.");
            return;
        }
        String estado = obtenerEstadoSeleccionado();
        if (estado == null) {
            mostrarError("Seleccione ENTREGADO o NO ENTREGADO.");
            return;
        }
        MetodoPago metodoPago = null;
        if (PedidoService.ENTREGADO.equals(estado)) {
            metodoPago = construirMetodoPago();
            if (metodoPago == null) {
                return;
            }
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar actualizacion");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("Confirma actualizar el pedido " + pedidoActual.getNroPedido() + "?");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        try {
            pedidoService.actualizarPedido(pedidoActual.getNroPedido(), estado, metodoPago, txtObservaciones.getText());
            cargarPedido(pedidoActual);
            btnConfirmar.setDisable(true);
            mostrarOk("Pedido actualizado correctamente.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        limpiarFormulario();
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) txtNumeroPedido.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private Integer leerNumeroPedido() {
        String texto = txtNumeroPedido.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("El numero de pedido es obligatorio.");
            return null;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            mostrarError("El numero de pedido debe ser numerico.");
            return null;
        }
    }

    private String obtenerEstadoSeleccionado() {
        if (rbEntregado.isSelected()) {
            return PedidoService.ENTREGADO;
        }
        if (rbNoEntregado.isSelected()) {
            return PedidoService.NO_ENTREGADO;
        }
        return null;
    }

    private void actualizarVisibilidadPago() {
        boolean entregado = rbEntregado.isSelected();
        boolean puedeEditar = pedidoActual != null && pedidoActual.estaPendiente();
        boxEstado.setDisable(!puedeEditar);
        boxMetodoPago.setDisable(!entregado || !puedeEditar);
        boxMetodoPago.setVisible(entregado);
        boxMetodoPago.setManaged(entregado);

        boolean contado = entregado && rbContado.isSelected();
        boolean electronico = entregado && rbElectronico.isSelected();
        boxContado.setVisible(contado);
        boxContado.setManaged(contado);
        boxContado.setDisable(!contado);
        boxElectronico.setVisible(electronico);
        boxElectronico.setManaged(electronico);
        boxElectronico.setDisable(!electronico);

        if (!entregado) {
            rbContado.setSelected(false);
            rbElectronico.setSelected(false);
            txtMontoRecibido.clear();
            txtEntidadPago.clear();
            txtNroOperacion.clear();
        }
    }

    private MetodoPago construirMetodoPago() {
        if (rbContado.isSelected()) {
            try {
                return new PagoContado(Double.parseDouble(txtMontoRecibido.getText().trim()));
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un monto contado valido.");
                return null;
            }
        }
        if (rbElectronico.isSelected()) {
            String entidad = txtEntidadPago.getText().trim();
            String operacion = txtNroOperacion.getText().trim();
            if (entidad.isEmpty() || operacion.isEmpty()) {
                mostrarError("Complete entidad y numero de operacion.");
                return null;
            }
            return new PagoElectronico(operacion, entidad);
        }
        mostrarError("Seleccione metodo de pago.");
        return null;
    }

    private void cargarPedido(Pedido pedido) {
        txtNumero.setText(String.valueOf(pedido.getNroPedido()));
        txtCliente.setText(pedido.getNombreCliente());
        txtDireccion.setText(pedido.getDireccionCliente());
        txtBarrio.setText(pedido.getBarrioCliente());
        txtZona.setText(pedido.getZonaCliente());
        txtFechaEmision.setText(pedido.getFechaEmision().toString());
        txtFechaEntrega.setText(pedido.getFechaEntrega().toString());
        txtTotal.setText(pedido.getTotalFormateado());
        txtEstadoActual.setText(pedido.getEstadoNombre());
        txtObservaciones.setText(pedido.getObservaciones());
        tblDetalles.setItems(FXCollections.observableArrayList(pedido.getDetalles()));
    }

    private void limpiarDetalle() {
        for (TextField campo : new TextField[]{txtNumero, txtCliente, txtDireccion, txtBarrio, txtZona,
                txtFechaEmision, txtFechaEntrega, txtTotal, txtEstadoActual}) {
            campo.clear();
        }
        tblDetalles.getItems().clear();
    }

    private void limpiarFormulario() {
        pedidoActual = null;
        txtNumeroPedido.clear();
        limpiarDetalle();
        estadoGroup.selectToggle(null);
        pagoGroup.selectToggle(null);
        txtMontoRecibido.clear();
        txtEntidadPago.clear();
        txtNroOperacion.clear();
        txtObservaciones.clear();
        deshabilitarActualizacion();
        actualizarVisibilidadPago();
        limpiarMensaje();
    }

    private void habilitarActualizacion() {
        boxEstado.setDisable(false);
        btnConfirmar.setDisable(false);
    }

    private void deshabilitarActualizacion() {
        boxEstado.setDisable(true);
        boxMetodoPago.setDisable(true);
        boxContado.setDisable(true);
        boxElectronico.setDisable(true);
        btnConfirmar.setDisable(true);
        estadoGroup.selectToggle(null);
        pagoGroup.selectToggle(null);
        actualizarVisibilidadPago();
    }

    private void limpiarMensaje() {
        lblMensaje.setText("");
        lblMensaje.setStyle("");
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #b00020;");
    }

    private void mostrarOk(String mensaje) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: #1b7f3a;");
    }
}
