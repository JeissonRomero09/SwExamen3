package co.edu.poli.Examen2.aplicacion.puerto.entrada;

import co.edu.poli.Examen2.dominio.Examen;

/**
 * Puerto de entrada para actualizar un examen existente.
 *
 * @author Jeisson Romero
 */
public interface EditarExamenUseCase {
    /**
     * Actualiza un examen, permitiendo cambiar su código.
     *
     * @param codigoActual código con el que está registrado
     * @param examen nuevos datos del examen
     */
    void editar(String codigoActual, Examen examen);
}
