package co.edu.poli.Examen2.aplicacion.servicio;

import co.edu.poli.Examen2.aplicacion.puerto.entrada.BuscarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.dominio.Examen;
import java.util.List;
import java.util.Optional;

/**
 * Implementa la búsqueda individual y el listado de exámenes.
 *
 * @author Jeisson Romero
 */
public final class BuscarExamenService implements BuscarExamenUseCase {
    private final RepositoryExamen repository;

    /** Crea el servicio con el port de persistencia requerido. */
    public BuscarExamenService(RepositoryExamen repository) {
        this.repository = repository;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Examen> buscar(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese el código del examen.");
        }
        return repository.buscarPorCodigo(codigo.trim());
    }

    /** {@inheritDoc} */
    @Override
    public List<Examen> listar() {
        return repository.listar();
    }
}
