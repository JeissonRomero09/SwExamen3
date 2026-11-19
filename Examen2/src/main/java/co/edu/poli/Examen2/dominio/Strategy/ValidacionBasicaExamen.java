package co.edu.poli.Examen2.dominio.Strategy;

import co.edu.poli.Examen2.dominio.Examen;

/**
 * Valida la presencia del examen y de sus datos de identificación.
 *
 * @author Jeisson Romero
 */
public final class ValidacionBasicaExamen implements EstrategiaValidacionExamen {
    /** {@inheritDoc} */
    @Override
    public boolean validar(Examen examen) {
        return examen != null
                && examen.getCodigo() != null
                && !examen.getCodigo().trim().isEmpty()
                && examen.getNombrePaciente() != null
                && !examen.getNombrePaciente().trim().isEmpty();
    }
}
