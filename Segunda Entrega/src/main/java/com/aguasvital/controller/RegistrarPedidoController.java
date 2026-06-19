package com.aguasvital.controller;

import com.aguasvital.model.*;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegistrarPedidoController {

    @FXML private TextField txtDocumento;
    @FXML private TextField txtCliente;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBarrioZona;
    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private Spinner<Integer> spnCantidad;
    @FXML private DatePicker dpFechaEntrega;
    @FXML private TableView<DetallePedido> tblProductos;
    @FXML private TableColumn<DetallePedido, String> colProducto;
    @FXML private TableColumn<DetallePedido, Integer> colCantidad;
    @FXML private TableColumn<DetallePedido, Double> colPrecio;
    @FXML private TableColumn<DetallePedido, Double> colSubtotal;
    @FXML private Label lblTotal;
    @FXML private Label lblMensaje;

    private final DatosSistema datos = DatosSistema.getInstancia();
    private final ObservableList<DetallePedido> listaItems = FXCollections.observableArrayList();
    private Cliente clienteActual;

    @FXML
    private void initialize() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitarioHistorico"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblProductos.setItems(listaItems);

        cmbProducto.setItems(FXCollections.observableArrayList(datos.getProductos()));

        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(svf);

        dpFechaEntrega.setValue(LocalDate.now());
        limpiarMensaje();
    }

    @FXML
    private void buscarCliente() {
        String dni = txtDocumento.getText().trim();
        if (dni.isEmpty()) {
            mostrarError("Ingrese un número de documento.");
            return;
        }

        datos.buscarClientePorDni(dni).ifPresentOrElse(cliente -> {
            clienteActual = cliente;
            txtCliente.setText(cliente.getNombreCompleto());
            txtTelefono.setText(cliente.getTelefono());
            txtDireccion.setText(cliente.getDireccion());

            Barrio barrio = cliente.getBarrio();
            if (barrio == null) {
                txtBarrioZona.setText("Sin barrio registrado");
                mostrarError("El cliente no tiene un barrio/zona registrado en el sistema.");
                return;
            }
            txtBarrioZona.setText(barrio.getNombre() + " — " + barrio.getZona().getNombre());
            mostrarOk("Cliente encontrado: " + cliente.getNombreCompleto());
        }, () -> {
            clienteActual = null;
            txtCliente.clear();
            txtTelefono.clear();
            txtDireccion.clear();
            txtBarrioZona.clear();
            mostrarError("Cliente no encontrado con documento: " + dni);
        });
    }

    @FXML
    private void agregarProducto() {
        Producto producto = cmbProducto.getValue();
        Integer cantidad = spnCantidad.getValue();

        if (producto == null) {
            mostrarError("Seleccione un producto de la lista.");
            return;
        }
        if (cantidad == null || cantidad <= 0) {
            mostrarError("Ingrese una cantidad válida.");
            return;
        }

        DetallePedido detalle = new DetallePedido(producto, cantidad);
        listaItems.add(detalle);
        actualizarTotal();
        mostrarOk("Producto agregado: " + producto.getNombre() + " x" + cantidad);
    }

    @FXML
    private void quitarProducto() {
        DetallePedido seleccionado = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un producto de la tabla para quitar.");
            return;
        }
        listaItems.remove(seleccionado);
        actualizarTotal();
        mostrarOk("Producto quitado: " + seleccionado.getNombreProducto());
    }

    @FXML
    private void confirmarPedido() {
        if (clienteActual == null) {
            mostrarError("Busque y seleccione un cliente antes de confirmar.");
            return;
        }
        if (listaItems.isEmpty()) {
            mostrarError("Agregue al menos un producto al pedido.");
            return;
        }

        Barrio barrio = clienteActual.getBarrio();
        if (barrio == null) {
            mostrarError("El cliente no tiene un barrio/zona registrado.");
            return;
        }

        LocalDate fechaEntrega = dpFechaEntrega.getValue();
        if (fechaEntrega == null) {
            mostrarError("Seleccione una fecha de entrega estimada.");
            return;
        }

        try {
            com.aguasvital.dao.PedidoDAOImpl pedidoDAO = new com.aguasvital.dao.PedidoDAOImpl();
            int nroPedido = pedidoDAO.generarNroPedido();

            Pedido nuevo = new Pedido(nroPedido, LocalDate.now(), fechaEntrega,
                    clienteActual, java.util.List.copyOf(listaItems));

            com.aguasvital.service.PedidoService service = new com.aguasvital.service.PedidoService();
            service.registrarPedido(nuevo);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Pedido Registrado");
            alerta.setHeaderText(null);
            alerta.setContentText("Pedido #" + nroPedido + " registrado correctamente.\nCliente: " + clienteActual.getNombreCompleto() + "\nTotal: $" + String.format("%.2f", nuevo.getTotal()));
            alerta.showAndWait();

            limpiarFormulario();
        } catch (Exception e) {
            mostrarError("Error al registrar pedido: " + e.getMessage());
        }
    }

    @FXML
    private void limpiarFormulario() {
        clienteActual = null;
        txtDocumento.clear();
        txtCliente.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtBarrioZona.clear();
        listaItems.clear();
        cmbProducto.setValue(null);
        spnCantidad.getValueFactory().setValue(1);
        dpFechaEntrega.setValue(LocalDate.now());
        actualizarTotal();
        limpiarMensaje();
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) lblTotal.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void actualizarTotal() {
        double total = listaItems.stream().mapToDouble(DetallePedido::getSubtotal).sum();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void mostrarError(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #b00020;");
        lblMensaje.setText(msg);
    }

    private void mostrarOk(String msg) {
        lblMensaje.setStyle("-fx-text-fill: #1b7f3a;");
        lblMensaje.setText(msg);
    }

    private void limpiarMensaje() {
        lblMensaje.setText("");
    }
}
