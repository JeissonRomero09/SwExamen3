package co.edu.poli.Examen2.aplicacion.puerto.entrada;

/**
 * Puerto de entrada para eliminar un examen por código.
 *
 * @author Jeisson Romero
 */
public interface EliminarExamenUseCase {
    /**
     * Elimina el examen indicado.
     *
     * @param codigo código del examen
     * @return {@code true} si se eliminó un registro
     */
    boolean eliminar(String codigo);
}
