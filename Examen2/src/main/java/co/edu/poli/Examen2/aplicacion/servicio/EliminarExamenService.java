package co.edu.poli.Examen2.aplicacion.servicio;

import co.edu.poli.Examen2.aplicacion.puerto.entrada.EliminarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;

/**
 * Implementa la eliminación de exámenes y valida el código recibido.
 *
 * @author Jeisson Romero
 */
public final class EliminarExamenService implements EliminarExamenUseCase {
    private final RepositoryExamen repository;

    /** Crea el servicio con el port de persistencia requerido. */
    public EliminarExamenService(RepositoryExamen repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public boolean eliminar(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese el código del examen que desea eliminar.");
        }
        return repository.eliminar(codigo.trim());
    }
}
