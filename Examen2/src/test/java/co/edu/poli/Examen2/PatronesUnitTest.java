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
import co.edu.poli.Examen2.dominio.Strategy.EstrategiaValidacionExamen;
import co.edu.poli.Examen2.dominio.Strategy.ValidacionBasicaExamen;
import co.edu.poli.Examen2.dominio.Strategy.ValidacionCompletaExamen;
import co.edu.poli.Examen2.dominio.Strategy.ValidadorExamen;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de los patrones Factory Method, Strategy y Adapter.
 *
 * @author Jeisson Romero
 */
class PatronesUnitTest {
    @Test
    void factoryMethodCreatesExam() {
        Examen examen = new ExamenSangreFactory().crearExamen(
                "EX-1", "Paciente Uno", 18.5, TipoSangre.AB, FactorRh.POSITIVO);

        assertEquals("EX-1", examen.getCodigo());
        assertEquals(TipoSangre.AB, examen.getTipoSangre());
        assertEquals(FactorRh.POSITIVO, examen.getFactorRh());
    }

    @Test
    void strategiesCanBeSelectedAndExecuted() {
        ValidadorExamen validador = new ValidadorExamen();
        Examen examen = examen("EX-2");

        assertThrows(IllegalStateException.class, () -> validador.ejecutarValidacion(examen));
        validador.setEstrategia(new ValidacionBasicaExamen());
        assertTrue(validador.ejecutarValidacion(examen));
        validador.setEstrategia(new ValidacionCompletaExamen());
        assertTrue(validador.ejecutarValidacion(examen));
        assertFalse(new ValidacionBasicaExamen().validar(null));
        assertThrows(IllegalArgumentException.class, () -> validador.setEstrategia(null));
    }

    @Test
    void adapterTranslatesRepositoryCallsToExternalService() {
        RepositorioExternoFalso externo = new RepositorioExternoFalso();
        RepositoryExamen repository = new ExamenRepositoryAdapter(externo);
        Examen examen = examen("EX-3");

        repository.crear(examen);

        assertEquals(examen, repository.buscarPorCodigo("EX-3").get());
        assertEquals(1, repository.listar().size());

        Examen actualizado = new Examen("EX-4", "Paciente Dos", 25,
                TipoSangre.O, FactorRh.NEGATIVO);
        repository.editar("EX-3", actualizado);
        assertTrue(repository.buscarPorCodigo("EX-3").isEmpty());
        assertEquals(actualizado, repository.buscarPorCodigo("EX-4").get());
        assertTrue(repository.eliminar("EX-4"));
        assertFalse(repository.eliminar("EX-4"));
    }

    @Test
    void adapterRejectsUnexpectedExternalResultType() {
        RepositorioExternoFalso externo = new RepositorioExternoFalso();
        externo.resultadoConsulta = "resultado no compatible";
        RepositoryExamen repository = new ExamenRepositoryAdapter(externo);

        assertThrows(IllegalStateException.class, () -> repository.buscarPorCodigo("EX-5"));
    }

    private Examen examen(String codigo) {
        return new Examen(codigo, "Paciente Uno", 10, TipoSangre.A, FactorRh.POSITIVO);
    }

    /**
     * Reemplazo en memoria del repositorio externo usado para aislar el Adapter.
     *
     * @author Jeisson Romero
     */
    private static final class RepositorioExternoFalso extends RepositorioExternoExamen {
        private final Map<String, Examen> examenes = new LinkedHashMap<>();
        private Object resultadoConsulta;

        private RepositorioExternoFalso() {
            super("jdbc:mariadb://localhost:3306/examenes", "test", "");
        }

        @Override
        public void guardarRegistro(Examen examen) {
            examenes.put(examen.getCodigo(), examen);
        }

        @Override
        public Object consultarPorCodigo(String codigo) {
            return resultadoConsulta == null ? examenes.get(codigo) : resultadoConsulta;
        }

        @Override
        public List<Examen> listarRegistros() {
            return new ArrayList<>(examenes.values());
        }

        @Override
        public void modificarRegistro(String codigoActual, Examen examen) {
            examenes.remove(codigoActual);
            examenes.put(examen.getCodigo(), examen);
        }

        @Override
        public void borrarRegistro(String codigo) {
            examenes.remove(codigo);
        }
    }
}
