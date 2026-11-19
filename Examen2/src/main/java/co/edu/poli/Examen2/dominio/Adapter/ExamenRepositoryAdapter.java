package co.edu.poli.Examen2.dominio.Adapter;

import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.dominio.Examen;
import java.util.List;
import java.util.Optional;

/**
 * Adapta el repositorio externo al contrato usado por la aplicación.
 *
 * @author Jeisson Romero
 */
public final class ExamenRepositoryAdapter implements RepositoryExamen {
    private final RepositorioExternoExamen servicio;

    /**
     * Crea el adaptador para el servicio externo.
     *
     * @param servicio repositorio externo que se adapta
     */
    public ExamenRepositoryAdapter(RepositorioExternoExamen servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El repositorio externo es obligatorio.");
        }
        this.servicio = servicio;
    }

    /** {@inheritDoc} */
    @Override
    public void crear(Examen examen) {
        servicio.guardarRegistro(examen);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Examen> buscarPorCodigo(String codigo) {
        Object resultado = servicio.consultarPorCodigo(codigo);
        if (resultado == null) {
            return Optional.empty();
        }
        if (!(resultado instanceof Examen)) {
            throw new IllegalStateException(
                    "El repositorio externo devolvió un tipo de examen no reconocido.");
        }
        return Optional.of((Examen) resultado);
    }

    /** {@inheritDoc} */
    @Override
    public List<Examen> listar() {
        return servicio.listarRegistros();
    }

    /** {@inheritDoc} */
    @Override
    public void editar(String codigoActual, Examen examen) {
        servicio.modificarRegistro(codigoActual, examen);
    }

    /** {@inheritDoc} */
    @Override
    public boolean eliminar(String codigo) {
        if (!buscarPorCodigo(codigo).isPresent()) {
            return false;
        }
        servicio.borrarRegistro(codigo);
        return true;
    }
}
