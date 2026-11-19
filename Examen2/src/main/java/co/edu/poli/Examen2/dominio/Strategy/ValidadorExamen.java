package co.edu.poli.Examen2.dominio.Strategy;

import co.edu.poli.Examen2.dominio.Examen;

/**
 * Ejecuta una estrategia de validación que puede cambiar en tiempo de ejecución.
 *
 * @author Jeisson Romero
 */
public final class ValidadorExamen {
    private EstrategiaValidacionExamen estrategia;

    /**
     * Selecciona la estrategia activa.
     *
     * @param estrategia estrategia que se ejecutará
     * @throws IllegalArgumentException si la estrategia es {@code null}
     */
    public void setEstrategia(EstrategiaValidacionExamen estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de validación es obligatoria.");
        }
        this.estrategia = estrategia;
    }

    /**
     * Ejecuta la estrategia seleccionada.
     *
     * @param examen examen que se valida
     * @return resultado de la validación
     * @throws IllegalStateException si todavía no se configuró una estrategia
     */
    public boolean ejecutarValidacion(Examen examen) {
        if (estrategia == null) {
            throw new IllegalStateException("Debe configurar una estrategia de validación.");
        }
        return estrategia.validar(examen);
    }
}
