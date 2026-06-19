package com.aguasvital.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:aguas_vital.db";
    private static DatabaseManager instancia;

    private DatabaseManager() {
        inicializar();
    }

    public static DatabaseManager getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseManager();
        }
        return instancia;
    }

    public static void reiniciar() {
        if (instancia != null) {
            try { instancia.droppearYReinicializar(); } catch (Exception e) {
                System.err.println("Error al reiniciar BD: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }

    private void inicializar() {
        ejecutarSQL("sql/esquema.sql");
    }

    private void droppearYReinicializar() {
        String dropSQL = "DROP TABLE IF EXISTS detalle_pedido; DROP TABLE IF EXISTS pedido; " +
                "DROP TABLE IF EXISTS empleado; DROP TABLE IF EXISTS cliente; " +
                "DROP TABLE IF EXISTS precio; DROP TABLE IF EXISTS producto; " +
                "DROP TABLE IF EXISTS envase; DROP TABLE IF EXISTS barrio; DROP TABLE IF EXISTS zona;";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String s : dropSQL.split(";")) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) stmt.execute(trimmed);
            }
        } catch (Exception e) {
            System.err.println("Error al droppear tablas: " + e.getMessage());
        }
        inicializar();
    }

    private void ejecutarSQL(String resourcePath) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            getClass().getClassLoader().getResourceAsStream(resourcePath)));
            String sql = reader.lines().collect(Collectors.joining("\n"));
            for (String sentencia : sql.split(";")) {
                String s = sentencia.trim();
                if (!s.isEmpty()) {
                    stmt.execute(s);
                }
            }
        } catch (Exception e) {
            System.err.println("DatabaseManager: " + e.getMessage());
        }
    }
}
