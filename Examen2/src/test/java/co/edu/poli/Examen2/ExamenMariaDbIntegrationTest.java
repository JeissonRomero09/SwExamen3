package co.edu.poli.Examen2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.dominio.Adapter.ExamenRepositoryAdapter;
import co.edu.poli.Examen2.dominio.Adapter.RepositorioExternoExamen;
import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;
import co.edu.poli.Examen2.dominio.Factory.ExamenSangreFactory;
import co.edu.poli.Examen2.dominio.Strategy.ValidacionCompletaExamen;
import co.edu.poli.Examen2.dominio.Strategy.ValidadorExamen;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

/**
 * Prueba los patrones Factory Method, Strategy y Adapter junto con MariaDB.
 * Ejecución opt-in: {@code mvn "-Dexamenes.integration=true" test}.
 *
 * @author Jeisson Romero
 */
@EnabledIfSystemProperty(named = "examenes.integration", matches = "true")
class ExamenMariaDbIntegrationTest {
    private RepositoryExamen repository;
    private String codigoOriginal;
    private String codigoEditado;
    private boolean datosInsertados;

    @BeforeEach
    void conectarRepositorio() {
        String identificador = UUID.randomUUID().toString().replace("-", "");
        codigoOriginal = "IT-" + identificador.substring(0, 16);
        codigoEditado = codigoOriginal + "-E";
        repository = crearRepositorio();
    }

    @AfterEach
    void eliminarDatosDePrueba() {
        if (repository != null && datosInsertados) {
            repository.eliminar(codigoOriginal);
            repository.eliminar(codigoEditado);
        }
    }

    @Test
    void factoryStrategyAdapterAndMariaDbSupportCrudAndPersistence() {
        ExamenSangreFactory factory = new ExamenSangreFactory();
        ValidadorExamen validador = new ValidadorExamen();
        validador.setEstrategia(new ValidacionCompletaExamen());

        Examen original = factory.crearExamen(
                codigoOriginal, "Paciente Integración", 45.75,
                TipoSangre.AB, FactorRh.POSITIVO);
        assertTrue(validador.ejecutarValidacion(original));

        repository.crear(original);
        datosInsertados = true;
        Examen guardado = repository.buscarPorCodigo(codigoOriginal).get();
        assertEquals(original.getCodigo(), guardado.getCodigo());
        assertEquals(original.getNombrePaciente(), guardado.getNombrePaciente());
        assertEquals(original.getCosto(), guardado.getCosto());
        assertEquals(original.getTipoSangre(), guardado.getTipoSangre());
        assertEquals(original.getFactorRh(), guardado.getFactorRh());
        assertTrue(repository.listar().stream()
                .anyMatch(examen -> examen.getCodigo().equals(codigoOriginal)));
        assertThrows(IllegalArgumentException.class, () -> repository.crear(original));

        Examen actualizado = factory.crearExamen(
                codigoEditado, "Paciente Actualizado", 50.25,
                TipoSangre.O, FactorRh.NEGATIVO);
        assertTrue(validador.ejecutarValidacion(actualizado));
        repository.editar(codigoOriginal, actualizado);

        RepositoryExamen nuevaInstancia = crearRepositorio();
        assertFalse(nuevaInstancia.buscarPorCodigo(codigoOriginal).isPresent());
        Examen recuperado = nuevaInstancia.buscarPorCodigo(codigoEditado).get();
        assertEquals("Paciente Actualizado", recuperado.getNombrePaciente());
        assertEquals(50.25, recuperado.getCosto());
        assertEquals(TipoSangre.O, recuperado.getTipoSangre());
        assertEquals(FactorRh.NEGATIVO, recuperado.getFactorRh());

        assertTrue(repository.eliminar(codigoEditado));
        assertFalse(nuevaInstancia.buscarPorCodigo(codigoEditado).isPresent());
        datosInsertados = false;
    }

    private RepositoryExamen crearRepositorio() {
        String url = System.getProperty("examenes.db.url",
                environmentOrDefault("EXAMENES_DB_URL",
                        "jdbc:mariadb://localhost:3306/examenes"));
        String usuario = System.getProperty("examenes.db.user",
                environmentOrDefault("EXAMENES_DB_USER", "root"));
        String contrasena = System.getProperty("examenes.db.password",
                environmentOrDefault("EXAMENES_DB_PASSWORD", ""));
        return new ExamenRepositoryAdapter(
                new RepositorioExternoExamen(url, usuario, contrasena));
    }

    private static String environmentOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
    }
}
