package co.edu.poli.Examen2.dominio.Factory;

import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;

/**
 * Fábrica concreta que crea exámenes de sangre.
 *
 * @author Jeisson Romero
 */
public final class ExamenSangreFactory extends ExamenFactory {
    /** {@inheritDoc} */
    @Override
    public Examen crearExamen(String codigo, String nombre, double costo,
                              TipoSangre tipo, FactorRh rh) {
        return new Examen(codigo, nombre, costo, tipo, rh);
    }
}
