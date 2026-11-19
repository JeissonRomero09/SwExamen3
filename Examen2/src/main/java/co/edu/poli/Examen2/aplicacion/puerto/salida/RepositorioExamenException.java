package co.edu.poli.Examen2.aplicacion.puerto.salida;

/**
 * Indica que una operación de persistencia no pudo completarse.
 *
 * @author Jeisson Romero
 */
public final class RepositorioExamenException extends RuntimeException {
    /**
     * Crea la excepción con su causa original.
     *
     * @param message descripción de la operación fallida
     * @param cause excepción de origen
     */
    public RepositorioExamenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message descripción del error
     */
    public RepositorioExamenException(String message) {
        super(message);
    }
}
