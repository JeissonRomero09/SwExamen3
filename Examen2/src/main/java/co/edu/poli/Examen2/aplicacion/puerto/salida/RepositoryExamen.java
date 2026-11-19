package co.edu.poli.Examen2.aplicacion.puerto.salida;

import co.edu.poli.Examen2.dominio.Examen;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida que abstrae las operaciones de persistencia de exámenes.
 *
 * @author Jeisson Romero
 */
public interface RepositoryExamen {
    /**
     * Persiste un nuevo examen.
     *
     * @param examen examen que se guarda
     */
    void crear(Examen examen);

    /**
     * Busca un examen por código.
     *
     * @param codigo código que se consulta
     * @return examen encontrado, si existe
     */
    Optional<Examen> buscarPorCodigo(String codigo);

    /** @return todos los exámenes persistidos */
    List<Examen> listar();

    /**
     * Actualiza el examen identificado por {@code codigoActual}.
     *
     * @param codigoActual código actual del examen
     * @param examen datos actualizados
     */
    void editar(String codigoActual, Examen examen);

    /**
     * Elimina un examen por código.
     *
     * @param codigo código que se elimina
     * @return {@code true} si existía y fue eliminado
     */
    boolean eliminar(String codigo);
}
