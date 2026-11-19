package co.edu.poli.Examen2.dominio;

/**
 * Entidad inmutable que representa un examen de sangre registrado.
 *
 * @author Jeisson Romero
 */
public final class Examen {

    private final String codigo;
    private final String nombrePaciente;
    private final double costo;
    private final TipoSangre tipoSangre;
    private final FactorRh factorRh;

    /**
     * Crea un examen validando sus atributos obligatorios y el costo.
     *
     * @param codigo código único del examen
     * @param nombrePaciente nombre del paciente
     * @param costo costo no negativo
     * @param tipoSangre tipo de sangre
     * @param factorRh factor Rh
     * @throws IllegalArgumentException si algún dato no cumple las reglas del dominio
     */
    public Examen(String codigo, String nombrePaciente, double costo,
                  TipoSangre tipoSangre, FactorRh factorRh) {
        this.codigo = requireText(codigo, "El código del examen es obligatorio.");
        this.nombrePaciente = requireText(nombrePaciente, "El nombre del paciente es obligatorio.");
        if (!Double.isFinite(costo) || costo < 0) {
            throw new IllegalArgumentException("El costo debe ser un número válido mayor o igual a cero.");
        }
        this.costo = costo;
        if (tipoSangre == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de sangre.");
        }
        if (factorRh == null) {
            throw new IllegalArgumentException("Debe seleccionar un factor Rh.");
        }
        this.tipoSangre = tipoSangre;
        this.factorRh = factorRh;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    /** @return código del examen */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del paciente */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /** @return costo del examen */
    public double getCosto() {
        return costo;
    }

    /** @return tipo de sangre del paciente */
    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    /** @return factor Rh del paciente */
    public FactorRh getFactorRh() {
        return factorRh;
    }
}
