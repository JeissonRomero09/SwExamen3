package co.edu.poli.Examen2.dominio.Strategy;

import co.edu.poli.Examen2.dominio.Examen;

/**
 * Valida los datos básicos y las invariantes completas del examen.
 *
 * @author Jeisson Romero
 */
public final class ValidacionCompletaExamen implements EstrategiaValidacionExamen {
    private final ValidacionBasicaExamen validacionBasica = new ValidacionBasicaExamen();

    /** {@inheritDoc} */
    @Override
    public boolean validar(Examen examen) {
        return validacionBasica.validar(examen)
                && Double.isFinite(examen.getCosto())
                && examen.getCosto() >= 0
                && examen.getTipoSangre() != null
                && examen.getFactorRh() != null;
    }
}
