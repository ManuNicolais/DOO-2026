package com.aguasvital.controller;

import com.aguasvital.model.Cargo;
import com.aguasvital.repository.UsuarioSistema;
import com.aguasvital.util.Navegador;
import com.aguasvital.util.SesionUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuPrincipalController {
    @FXML private Label lblUsuario;
    @FXML private Label lblCargo;
    @FXML private Button btnActualizarPedido;
    @FXML private Button btnRegistrarPendientes;
    @FXML private Button btnGenerarRendicion;
    @FXML private Button btnGestionarPrecios;
    @FXML private Button btnConsultarPedidos;
    @FXML private Button btnRegistrarPedido;
    @FXML private Button btnRegistrarCliente;
    @FXML private Button btnCancelarPedido;
    @FXML private Button btnEfectuarTraslado;

    @FXML
    private void initialize() {
        UsuarioSistema usuario = SesionUsuario.getUsuarioActivo();
        if (usuario == null) {
            return;
        }
        lblUsuario.setText(usuario.getUsuario());
        lblCargo.setText(usuario.getCargo().toString());
        configurarOpciones(usuario.getCargo());
    }

    private void configurarOpciones(Cargo cargo) {
        boolean administrativo = cargo == Cargo.ENCARGADO_ADMINISTRATIVO;
        boolean presidente = cargo == Cargo.PRESIDENTE;
        btnActualizarPedido.setVisible(administrativo);
        btnRegistrarPendientes.setVisible(administrativo);
        btnGenerarRendicion.setVisible(administrativo || presidente);
        btnGestionarPrecios.setVisible(administrativo || presidente);
        btnConsultarPedidos.setVisible(administrativo || presidente);

        btnRegistrarPedido.setVisible(cargo == Cargo.OPERADORA);
        btnRegistrarCliente.setVisible(cargo == Cargo.OPERADORA);
        btnCancelarPedido.setVisible(cargo == Cargo.OPERADORA);
        btnEfectuarTraslado.setVisible(cargo == Cargo.DISTRIBUIDOR);

        if (presidente) {
            btnGenerarRendicion.setText("Consultar Rendiciones");
            btnGestionarPrecios.setText("Consultar Precios");
        } else {
            btnGenerarRendicion.setText("Generar Rendicion");
            btnGestionarPrecios.setText("Gestionar Precios");
        }

        for (Button button : new Button[]{btnActualizarPedido, btnRegistrarPendientes, btnGenerarRendicion,
                btnGestionarPrecios, btnConsultarPedidos, btnRegistrarPedido, btnRegistrarCliente,
                btnCancelarPedido, btnEfectuarTraslado}) {
            button.setManaged(button.isVisible());
        }
    }

    @FXML private void abrirRegistrarCliente() { abrir("RegistrarCliente.fxml", "Registrar Cliente"); }
    @FXML private void abrirRegistrarPedido() { abrir("RegistrarPedido.fxml", "Registrar Pedido"); }
    @FXML private void abrirCancelarPedido() { abrir("CancelarPedido.fxml", "Cancelar Pedido"); }
    @FXML private void abrirActualizarPedido() { abrir("ActualizarPedido.fxml", "Actualizar Pedido"); }
    @FXML private void abrirRegistrarPendientes() { abrir("RegistrarPendientes.fxml", "Registrar Pendientes"); }
    @FXML private void abrirEfectuarTraslado() { abrir("EfectuarTraslado.fxml", "Efectuar Traslado"); }
    @FXML private void abrirGenerarRendicion() { abrir("GenerarRendicion.fxml", "Generar Rendicion Diaria"); }
    @FXML private void abrirGestionarPrecios() { abrir("GestionarPrecios.fxml", "Gestionar Precios"); }
    @FXML private void abrirConsultarPedidos() { abrir("ConsultarPedidos.fxml", "Consultar Pedidos"); }

    @FXML
    private void cerrarSesion() {
        SesionUsuario.cerrar();
        Stage stage = (Stage) lblUsuario.getScene().getWindow();
        Navegador.cambiarVista(stage, "Login.fxml", "Aguas Vital - Login", 420, 370);
    }

    private void abrir(String fxml, String titulo) {
        Stage stage = (Stage) lblUsuario.getScene().getWindow();
        Navegador.cambiarVista(stage, fxml, "Aguas Vital - " + titulo, 950, 680);
    }
}
