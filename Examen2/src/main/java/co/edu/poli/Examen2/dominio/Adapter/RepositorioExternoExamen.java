package co.edu.poli.Examen2.dominio.Adapter;

import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositorioExamenException;
import co.edu.poli.Examen2.infraestructura.adaptador.salida.persistencia.ConexionBD;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de persistencia externo adaptado a la interfaz de la aplicación.
 *
 * @author Jeisson Romero
 */
public class RepositorioExternoExamen {
    private static final String INSERT_SQL =
            "INSERT INTO examen (codigo, nombre_paciente, costo, tipo_sangre, factor_rh) "
                    + "VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_COLUMNS =
            "codigo, nombre_paciente, costo, tipo_sangre, factor_rh";

    private final ConexionBD conexionBD;

    /**
     * Configura la conexión a MariaDB.
     *
     * @param url URL JDBC de la base de datos
     * @param usuario usuario de la base de datos
     * @param contrasena contraseña de la base de datos
     */
    public RepositorioExternoExamen(String url, String usuario, String contrasena) {
        this.conexionBD = new ConexionBD(url, usuario, contrasena);
    }

    /**
     * Inserta un examen en la base de datos.
     *
     * @param examen examen que se guarda
     */
    public void guardarRegistro(Examen examen) {
        try (Connection connection = conexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            agregarParametros(statement, examen);
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("Ya existe un examen con ese código.");
            }
            throw errorBaseDatos("guardar el examen", e);
        }
    }

    /**
     * Busca un examen por código.
     *
     * @param codigo código que se consulta
     * @return el examen encontrado o {@code null}
     */
    public Object consultarPorCodigo(String codigo) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM examen WHERE codigo = ?";
        try (Connection connection = conexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        } catch (SQLException e) {
            throw errorBaseDatos("consultar el examen", e);
        }
    }

    /** @return todos los exámenes ordenados por código */
    public List<Examen> listarRegistros() {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM examen ORDER BY codigo";
        List<Examen> examenes = new ArrayList<>();
        try (Connection connection = conexionBD.obtenerConexion();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                examenes.add(mapear(result));
            }
            return examenes;
        } catch (SQLException e) {
            throw errorBaseDatos("listar los exámenes", e);
        }
    }

    /**
     * Actualiza un examen usando su código anterior como criterio.
     *
     * @param codigoActual código con el que está persistido
     * @param examen nuevos datos del examen
     */
    public void modificarRegistro(String codigoActual, Examen examen) {
        String sql = "UPDATE examen SET codigo = ?, nombre_paciente = ?, costo = ?, "
                + "tipo_sangre = ?, factor_rh = ? WHERE codigo = ?";
        try (Connection connection = conexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            agregarParametros(statement, examen);
            statement.setString(6, codigoActual);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el examen que se desea editar.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new IllegalArgumentException("Ya existe un examen con el nuevo código.");
            }
            throw errorBaseDatos("modificar el examen", e);
        }
    }

    /**
     * Elimina el registro indicado por código.
     *
     * @param codigo código del registro
     */
    public void borrarRegistro(String codigo) {
        try (Connection connection = conexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM examen WHERE codigo = ?")) {
            statement.setString(1, codigo);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw errorBaseDatos("borrar el examen", e);
        }
    }

    private void agregarParametros(PreparedStatement statement, Examen examen)
            throws SQLException {
        statement.setString(1, examen.getCodigo());
        statement.setString(2, examen.getNombrePaciente());
        statement.setBigDecimal(3, BigDecimal.valueOf(examen.getCosto()));
        statement.setString(4, examen.getTipoSangre().name());
        statement.setString(5, examen.getFactorRh().name());
    }

    private Examen mapear(ResultSet result) throws SQLException {
        return new Examen(
                result.getString("codigo"),
                result.getString("nombre_paciente"),
                result.getBigDecimal("costo").doubleValue(),
                TipoSangre.valueOf(result.getString("tipo_sangre")),
                FactorRh.valueOf(result.getString("factor_rh")));
    }

    private RepositorioExamenException errorBaseDatos(String operacion, SQLException error) {
        return new RepositorioExamenException(
                "No se pudo " + operacion + ": " + error.getMessage(), error);
    }
}
