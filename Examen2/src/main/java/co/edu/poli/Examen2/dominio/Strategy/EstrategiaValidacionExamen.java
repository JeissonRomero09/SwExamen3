package co.edu.poli.Examen2.dominio.Strategy;

import co.edu.poli.Examen2.dominio.Examen;

/**
 * Estrategia intercambiable para validar una entidad de examen.
 *
 * @author Jeisson Romero
 */
public interface EstrategiaValidacionExamen {
    /**
     * Evalúa el examen según las reglas de la estrategia.
     *
     * @param examen examen que se valida
     * @return {@code true} si cumple las reglas
     */
    boolean validar(Examen examen);
}
