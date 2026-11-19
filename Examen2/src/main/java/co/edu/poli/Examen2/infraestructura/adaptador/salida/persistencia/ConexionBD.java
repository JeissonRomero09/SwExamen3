package co.edu.poli.Examen2.infraestructura.adaptador.salida.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Crea conexiones JDBC a la base MariaDB configurada.
 *
 * @author Jeisson Romero
 */
public final class ConexionBD {
    private final String url;
    private final String usuario;
    private final String contrasena;

    /**
     * Configura las credenciales de conexión.
     *
     * @param url URL JDBC de MariaDB
     * @param usuario usuario de la base de datos
     * @param contrasena contraseña, o vacía si no se configuró
     */
    public ConexionBD(String url, String usuario, String contrasena) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de MariaDB es obligatoria.");
        }
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario de MariaDB es obligatorio.");
        }
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena == null ? "" : contrasena;
    }

    /**
     * Abre una conexión JDBC.
     *
     * @return conexión abierta
     * @throws SQLException si no puede establecerse la conexión
     */
    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, usuario, contrasena);
    }
}
