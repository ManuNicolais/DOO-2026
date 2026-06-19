package com.aguasvital.fxml;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.fail;

class FxmlLoadTest {
    private static final String[] FXMLS = {
            "Login.fxml",
            "MenuPrincipal.fxml",
            "RegistrarCliente.fxml",
            "RegistrarPedido.fxml",
            "CancelarPedido.fxml",
            "ActualizarPedido.fxml",
            "RegistrarPendientes.fxml",
            "EfectuarTraslado.fxml",
            "GenerarRendicion.fxml",
            "GestionarPrecios.fxml",
            "ConsultarPedidos.fxml"
    };

    @BeforeAll
    static void iniciarJavaFx() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException ignored) {
            // JavaFX ya fue iniciado por otra prueba.
        }
    }

    @Test
    void todosLosFxmlCarganConFxmlLoader() throws Exception {
        for (String fxml : FXMLS) {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Throwable> error = new AtomicReference<>();
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/aguasvital/view/" + fxml));
                    loader.load();
                } catch (Throwable throwable) {
                    error.set(throwable);
                } finally {
                    latch.countDown();
                }
            });
            latch.await();
            if (error.get() != null) {
                fail("No carga " + fxml + ": " + error.get().getMessage());
            }
        }
    }
}
