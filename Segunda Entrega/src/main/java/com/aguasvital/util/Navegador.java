package com.aguasvital.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navegador {
    private Navegador() {
    }

    public static void cambiarVista(Stage stage, String fxml, String titulo, double ancho, double alto) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/com/aguasvital/view/" + fxml));
            Parent root = loader.load();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root, ancho, alto));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar la vista " + fxml, e);
        }
    }
}
