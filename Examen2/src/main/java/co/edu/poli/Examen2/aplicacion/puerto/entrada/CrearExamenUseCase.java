package co.edu.poli.Examen2.aplicacion.puerto.entrada;

import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;

/**
 * Puerto de entrada para solicitar la creación de un examen.
 *
 * @author Jeisson Romero
 */
public interface CrearExamenUseCase {
    /**
     * Crea un examen a partir de sus datos de entrada.
     *
     * @param codigo código del examen
     * @param nombre nombre del paciente
     * @param costo costo del examen
     * @param tipo tipo de sangre
     * @param rh factor Rh
     */
    void crear(String codigo, String nombre, double costo, TipoSangre tipo, FactorRh rh);
}
