package com.aguasvital.controller;

import com.aguasvital.model.Cargo;
import com.aguasvital.model.Producto;
import com.aguasvital.repository.DatosSistema;
import com.aguasvital.util.Navegador;
import com.aguasvital.util.SesionUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;

public class GestionarPreciosController {
    @FXML private TableView<ProductoPrecioView> tblPrecios;
    @FXML private TableColumn<ProductoPrecioView, String> colCodigo;
    @FXML private TableColumn<ProductoPrecioView, String> colProducto;
    @FXML private TableColumn<ProductoPrecioView, String> colEnvase;
    @FXML private TableColumn<ProductoPrecioView, Integer> colCapacidad;
    @FXML private TableColumn<ProductoPrecioView, String> colPrecioActual;
    @FXML private TableColumn<ProductoPrecioView, String> colFechaDesde;
    @FXML private TextField txtNuevoPrecio;
    @FXML private DatePicker dpNuevaFechaDesde;
    @FXML private Button btnModificarPrecio;

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colEnvase.setCellValueFactory(new PropertyValueFactory<>("envase"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colPrecioActual.setCellValueFactory(new PropertyValueFactory<>("precioActual"));
        colFechaDesde.setCellValueFactory(new PropertyValueFactory<>("fechaDesde"));
        tblPrecios.setItems(FXCollections.observableArrayList(
                DatosSistema.getInstancia().getProductos().stream().map(ProductoPrecioView::new).toList()));
        if (SesionUsuario.getUsuarioActivo() != null
                && SesionUsuario.getUsuarioActivo().getCargo() == Cargo.PRESIDENTE) {
            btnModificarPrecio.setDisable(true);
        }
    }

    @FXML
    private void modificarPrecio() {
        ProductoPrecioView seleccionado = tblPrecios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Seleccione un producto de la tabla.");
            return;
        }
        double nuevoPrecio;
        try {
            nuevoPrecio = Double.parseDouble(txtNuevoPrecio.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Ingrese un precio numerico valido.");
            return;
        }
        if (nuevoPrecio <= 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "El precio debe ser mayor a cero.");
            return;
        }

        LocalDate fechaDesde = dpNuevaFechaDesde.getValue() == null ? LocalDate.now() : dpNuevaFechaDesde.getValue();
        seleccionado.actualizarPrecio(nuevoPrecio, fechaDesde);
        cargarPrecios();
        limpiar();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Precio actualizado correctamente.");
    }

    @FXML
    private void limpiar() {
        txtNuevoPrecio.clear();
        dpNuevaFechaDesde.setValue(null);
    }

    @FXML
    private void volverMenu() {
        Stage stage = (Stage) tblPrecios.getScene().getWindow();
        Navegador.cambiarVista(stage, "MenuPrincipal.fxml", "Aguas Vital - Menu Principal", 900, 620);
    }

    private void cargarPrecios() {
        tblPrecios.setItems(FXCollections.observableArrayList(
                DatosSistema.getInstancia().getProductos().stream().map(ProductoPrecioView::new).toList()));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Gestionar precios");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static class ProductoPrecioView {
        private final Producto producto;

        ProductoPrecioView(Producto producto) {
            this.producto = producto;
        }

        public String getCodigo() { return producto.getCodigo(); }
        public String getProducto() { return producto.getNombre(); }
        public String getEnvase() { return producto.getEnvase().getTipoEnvase(); }
        public int getCapacidad() { return producto.getEnvase().getCapacidad(); }
        public String getPrecioActual() { return "$" + String.format("%.2f", producto.getPrecioActual()); }
        public String getFechaDesde() {
            return producto.getPrecios().stream()
                    .filter(precio -> precio.estaVigente(LocalDate.now()))
                    .findFirst()
                    .orElse(producto.getPrecios().get(0))
                    .getFechaDesde()
                    .toString();
        }

        public void actualizarPrecio(double nuevoPrecio, LocalDate fechaDesde) {
            producto.getPrecios().stream()
                    .filter(precio -> precio.estaVigente(fechaDesde))
                    .forEach(precio -> precio.cerrarVigencia(fechaDesde.minusDays(1)));
            producto.agregarPrecio(new com.aguasvital.model.Precio(nuevoPrecio, fechaDesde, null));
        }
    }
}
