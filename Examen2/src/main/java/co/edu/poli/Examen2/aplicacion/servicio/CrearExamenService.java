package co.edu.poli.Examen2.aplicacion.servicio;

import co.edu.poli.Examen2.aplicacion.puerto.entrada.CrearExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;
import co.edu.poli.Examen2.dominio.Factory.ExamenFactory;
import co.edu.poli.Examen2.dominio.Strategy.ValidadorExamen;

/**
 * Implementa el caso de uso de creación usando fábrica, estrategia y port out.
 *
 * @author Jeisson Romero
 */
public final class CrearExamenService implements CrearExamenUseCase {
    private final RepositoryExamen repository;
    private final ExamenFactory examenFactory;
    private final ValidadorExamen validador;

    /**
     * Crea el servicio con sus dependencias de aplicación y dominio.
     *
     * @param repository puerto de persistencia
     * @param examenFactory fábrica de entidades
     * @param validador estrategia de validación
     */
    public CrearExamenService(RepositoryExamen repository, ExamenFactory examenFactory,
                              ValidadorExamen validador) {
        this.repository = repository;
        this.examenFactory = examenFactory;
        this.validador = validador;
    }

    /** {@inheritDoc} */
    @Override
    public void crear(String codigo, String nombre, double costo,
                      TipoSangre tipo, FactorRh rh) {
        Examen examen = examenFactory.crearExamen(codigo, nombre, costo, tipo, rh);
        if (!validador.ejecutarValidacion(examen)) {
            throw new IllegalArgumentException("El examen no cumple las reglas de validación.");
        }
        if (repository.buscarPorCodigo(examen.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un examen con ese código.");
        }
        repository.crear(examen);
    }
}
