package co.edu.poli.Examen2.aplicacion.servicio;

import co.edu.poli.Examen2.aplicacion.puerto.entrada.EditarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.dominio.Examen;

/**
 * Implementa la edición y sus verificaciones de existencia y unicidad.
 *
 * @author Jeisson Romero
 */
public final class EditarExamenService implements EditarExamenUseCase {
    private final RepositoryExamen repository;

    /** Crea el servicio con el port de persistencia requerido. */
    public EditarExamenService(RepositoryExamen repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public void editar(String codigoActual, Examen examen) {
        if (codigoActual == null || codigoActual.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese el código del examen que desea editar.");
        }
        codigoActual = codigoActual.trim();
        if (!repository.buscarPorCodigo(codigoActual).isPresent()) {
            throw new IllegalArgumentException("No existe el examen que se desea editar.");
        }
        if (!codigoActual.equals(examen.getCodigo())
                && repository.buscarPorCodigo(examen.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un examen con el nuevo código.");
        }
        repository.editar(codigoActual, examen);
    }
}
