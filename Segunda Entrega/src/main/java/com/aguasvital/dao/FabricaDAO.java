package com.aguasvital.dao;

/**
 * Fábrica que implementa el patrón Factory Method para crear instancias de DAOs.
 * <p>
 * Desacopla el código cliente de las implementaciones concretas de DAO.
 * Si en el futuro se agregara un nuevo motor de BD (ej: H2, MySQL), bastaría con
 * crear nuevas implementaciones y modificar esta fábrica; el resto del código
 * no se vería afectado porque depende de la interfaz {@link Dao}.
 * </p>
 * Patrón: Factory Method — centraliza la creación de objetos DAO.
 */
public class FabricaDAO {

    private FabricaDAO() {}

    /**
     * Crea y devuelve un DAO del tipo solicitado.
     * Lanza IllegalArgumentException si el tipo no está soportado.
     */
    @SuppressWarnings("unchecked")
    public static <T> Dao<T> fabricar(Class<T> tipoDTO) {
        String nombre = tipoDTO.getSimpleName();
        return switch (nombre) {
            case "ClienteDTO" -> (Dao<T>) new ClienteDAOImpl();
            case "PedidoDTO"  -> (Dao<T>) new PedidoDAOImpl();
            default -> throw new IllegalArgumentException("DAO no soportado: " + nombre);
        };
    }
}
