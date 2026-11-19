package co.edu.poli.Examen2.aplicacion.puerto.entrada;

import co.edu.poli.Examen2.dominio.Examen;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para consultar exámenes registrados.
 *
 * @author Jeisson Romero
 */
public interface BuscarExamenUseCase {
    /**
     * Busca un examen por su código.
     *
     * @param codigo código que se consulta
     * @return examen encontrado o {@link Optional#empty()} si no existe
     */
    Optional<Examen> buscar(String codigo);

    /** @return exámenes registrados */
    List<Examen> listar();
}
