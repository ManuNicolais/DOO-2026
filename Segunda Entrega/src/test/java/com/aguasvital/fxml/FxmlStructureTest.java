package com.aguasvital.fxml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FxmlStructureTest {
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

    @Test
    void todosLosFxmlTienenControllerYActionsExistentes() throws Exception {
        for (String fxml : FXMLS) {
            Document document = leer(fxml);
            Element root = document.getDocumentElement();
            assertFalse(root.getAttribute("xmlns").contains("javafx/" + "26"), fxml);
            String controller = root.getAttribute("fx:controller");
            assertFalse(controller.isBlank(), fxml + " sin controller");
            Class<?> controllerClass = Class.forName(controller);
            Set<String> metodos = new HashSet<>();
            for (Method method : controllerClass.getDeclaredMethods()) {
                metodos.add(method.getName());
            }
            NodeList nodes = document.getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                if (element.hasAttribute("onAction")) {
                    String action = element.getAttribute("onAction").replace("#", "");
                    assertTrue(metodos.contains(action), fxml + " referencia accion inexistente: " + action);
                }
            }
        }
    }

    private Document leer(String fxml) throws Exception {
        String path = "/com/aguasvital/view/" + fxml;
        InputStream inputStream = getClass().getResourceAsStream(path);
        assertNotNull(inputStream, "No existe " + path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        return factory.newDocumentBuilder().parse(inputStream);
    }
}
