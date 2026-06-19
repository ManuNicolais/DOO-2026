package com.aguasvital.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica DAO (Data Access Object).
 * <p>
 * Define las operaciones CRUD básicas que cualquier DAO concreto debe implementar.
 * El parámetro T representa el DTO que el DAO gestiona, asegurando que la capa
 * de persistencia nunca exponga objetos del modelo de dominio directamente.
 * </p>
 * Patrón: DAO — separa la lógica de acceso a datos de la lógica de negocio.
 *
 * @param <T> Tipo del DTO
 */
public interface Dao<T> {

    Optional<T> buscar(int id);

    List<T> listarTodos();

    void insertar(T dto);

    void modificar(T dto);

    void borrar(int id);
}
