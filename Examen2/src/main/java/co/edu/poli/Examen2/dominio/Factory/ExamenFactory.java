package co.edu.poli.Examen2.dominio.Factory;

import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;

/**
 * Define el método de fábrica para crear exámenes.
 *
 * @author Jeisson Romero
 */
public abstract class ExamenFactory {
    /**
     * Crea una entidad de examen con los datos proporcionados.
     *
     * @param codigo código del examen
     * @param nombre nombre del paciente
     * @param costo costo del examen
     * @param tipo tipo de sangre
     * @param rh factor Rh
     * @return examen creado
     */
    public abstract Examen crearExamen(String codigo, String nombre, double costo,
                                       TipoSangre tipo, FactorRh rh);
}
