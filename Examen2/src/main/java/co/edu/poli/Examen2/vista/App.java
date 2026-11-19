package co.edu.poli.Examen2.vista;

import java.io.IOException;

import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositoryExamen;
import co.edu.poli.Examen2.aplicacion.servicio.BuscarExamenService;
import co.edu.poli.Examen2.aplicacion.servicio.CrearExamenService;
import co.edu.poli.Examen2.aplicacion.servicio.EditarExamenService;
import co.edu.poli.Examen2.aplicacion.servicio.EliminarExamenService;
import co.edu.poli.Examen2.dominio.Adapter.ExamenRepositoryAdapter;
import co.edu.poli.Examen2.dominio.Adapter.RepositorioExternoExamen;
import co.edu.poli.Examen2.dominio.Factory.ExamenSangreFactory;
import co.edu.poli.Examen2.dominio.Strategy.ValidadorExamen;
import co.edu.poli.Examen2.dominio.Strategy.ValidacionCompletaExamen;
import co.edu.poli.Examen2.infraestructura.adaptador.entrada.javafx.PrimaryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Arranca la interfaz JavaFX y compone sus dependencias de aplicación.
 *
 * @author Jeisson Romero
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Carga la vista principal y presenta la ventana de la aplicación.
     *
     * @param stage ventana principal de JavaFX
     * @throws IOException si no se puede cargar la vista
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/co/edu/poli/Examen2/primary"), 640, 660);
        stage.setTitle("Exámenes de Sangre");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Cambia la vista raíz de la escena activa.
     *
     * @param fxml nombre del recurso FXML sin extensión
     * @throws IOException si no se puede cargar el recurso
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga una vista y configura los servicios, la base de datos y el controlador.
     *
     * @param fxml nombre del recurso FXML sin extensión
     * @return nodo raíz de la vista
     * @throws IOException si no se puede cargar el recurso
     */
    private static Parent loadFXML(String fxml) throws IOException {
        String databaseUrl = System.getenv("EXAMENES_DB_URL");
        if (databaseUrl == null || databaseUrl.trim().isEmpty()) {
            databaseUrl = "jdbc:mariadb://localhost:3306/examenes";
        }
        String databaseUser = System.getenv("EXAMENES_DB_USER");
        if (databaseUser == null || databaseUser.trim().isEmpty()) {
            databaseUser = "root";
        }
        String databasePassword = System.getenv("EXAMENES_DB_PASSWORD");
        if (databasePassword == null) {
            databasePassword = "";
        }
        RepositoryExamen repository = new ExamenRepositoryAdapter(
                new RepositorioExternoExamen(databaseUrl, databaseUser, databasePassword));
        ValidadorExamen validador = new ValidadorExamen();
        validador.setEstrategia(new ValidacionCompletaExamen());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(controllerType -> new PrimaryController(
                new CrearExamenService(repository, new ExamenSangreFactory(), validador),
                new BuscarExamenService(repository),
                new EditarExamenService(repository),
                new EliminarExamenService(repository)));
        return fxmlLoader.load();
    }

    /** Punto de entrada de la aplicación JavaFX. */
    public static void main(String[] args) {
        launch();
    }

}