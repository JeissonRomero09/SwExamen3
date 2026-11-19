package co.edu.poli.Examen2.infraestructura.adaptador.entrada.javafx;

import co.edu.poli.Examen2.aplicacion.puerto.entrada.BuscarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.entrada.CrearExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.entrada.EditarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.entrada.EliminarExamenUseCase;
import co.edu.poli.Examen2.aplicacion.puerto.salida.RepositorioExamenException;
import co.edu.poli.Examen2.dominio.Examen;
import co.edu.poli.Examen2.dominio.FactorRh;
import co.edu.poli.Examen2.dominio.TipoSangre;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador JavaFX que adapta las acciones de la pantalla a los casos de uso.
 *
 * @author Jeisson Romero
 */
public class PrimaryController {
	private final CrearExamenUseCase crearExamen;
	private final BuscarExamenUseCase buscarExamen;
	private final EditarExamenUseCase editarExamen;
	private final EliminarExamenUseCase eliminarExamen;
	private String codigoSeleccionado;

	/**
	 * Construye el controlador con los casos de uso requeridos por la pantalla.
	 *
	 * @param crearExamen caso de uso de creación
	 * @param buscarExamen casos de uso de búsqueda y listado
	 * @param editarExamen caso de uso de edición
	 * @param eliminarExamen caso de uso de eliminación
	 */
	public PrimaryController(CrearExamenUseCase crearExamen,
			BuscarExamenUseCase buscarExamen,
			EditarExamenUseCase editarExamen,
			EliminarExamenUseCase eliminarExamen) {
		this.crearExamen = crearExamen;
		this.buscarExamen = buscarExamen;
		this.editarExamen = editarExamen;
		this.eliminarExamen = eliminarExamen;
	}

	@FXML
	private TextField txtCodigoExamen;
	@FXML
	private TextField txtNombrePaciente;
	@FXML
	private TextField txtCosto;
	@FXML
	private ComboBox<String> cmbTipoSangre;
	@FXML
	private RadioButton rbPositivo;
	@FXML
	private RadioButton rbNegativo;
	@FXML
	private ToggleGroup tgFactorRH;
	@FXML
	private TextArea txtAreaMensajes;

	/** Inicializa el controlador cuando JavaFX carga el FXML. */
	@FXML
	public void initialize() {
	}

	/** Guarda un examen usando los valores ingresados en el formulario. */
	@FXML
	private void handleGuardar() {
		try {
			Examen examen = leerExamen();
			crearExamen.crear(examen.getCodigo(), examen.getNombrePaciente(), examen.getCosto(),
					examen.getTipoSangre(), examen.getFactorRh());
			codigoSeleccionado = examen.getCodigo();
			mostrarMensaje("Examen guardado correctamente.");
		} catch (RepositorioExamenException | IllegalArgumentException e) {
			mostrarError(e);
		}
	}

	/** Busca un examen por el código ingresado. */
	@FXML
	private void handleBuscar() {
		try {
			String codigo = txtCodigoExamen.getText().trim();
			Optional<Examen> encontrado = buscarExamen.buscar(codigo);
			if (encontrado.isPresent()) {
				codigoSeleccionado = encontrado.get().getCodigo();
				mostrarExamen(encontrado.get());
				mostrarMensaje("Examen encontrado.");
			} else {
				codigoSeleccionado = null;
				mostrarMensaje("No existe un examen con ese código.");
			}
		} catch (RepositorioExamenException | IllegalArgumentException e) {
			mostrarError(e);
		}
	}

	/** Actualiza el examen seleccionado con los valores del formulario. */
	@FXML
	private void handleEditar() {
		try {
			String codigoActual = codigoSeleccionado == null
					? txtCodigoExamen.getText().trim() : codigoSeleccionado;
			Examen examen = leerExamen();
			editarExamen.editar(codigoActual, examen);
			codigoSeleccionado = examen.getCodigo();
			mostrarMensaje("Examen actualizado correctamente.");
		} catch (RepositorioExamenException | IllegalArgumentException e) {
			mostrarError(e);
		}
	}

	/** Elimina el examen correspondiente al código ingresado. */
	@FXML
	private void handleEliminar() {
		try {
			String codigo = txtCodigoExamen.getText().trim();
			if (eliminarExamen.eliminar(codigo)) {
				codigoSeleccionado = null;
				mostrarMensaje("Examen eliminado correctamente.");
			} else {
				mostrarMensaje("No existe un examen con ese código.");
			}
		} catch (RepositorioExamenException | IllegalArgumentException e) {
			mostrarError(e);
		}
	}

	/** Restablece los campos y mensajes del formulario. */
	@FXML
	private void handleLimpiar() {
		txtCodigoExamen.clear();
		txtNombrePaciente.clear();
		txtCosto.clear();
		cmbTipoSangre.getSelectionModel().clearSelection();
		rbPositivo.setSelected(true);
		codigoSeleccionado = null;
		txtAreaMensajes.clear();
	}

	/** Presenta todos los exámenes persistidos. */
	@FXML
	private void handleListar() {
		try {
			StringBuilder mensaje = new StringBuilder("Exámenes registrados:\n");
			for (Examen examen : buscarExamen.listar()) {
				mensaje.append(formatear(examen)).append('\n');
			}
			txtAreaMensajes.setText(mensaje.toString());
		} catch (RepositorioExamenException e) {
			mostrarError(e);
		}
	}

	private Examen leerExamen() {
		String codigo = txtCodigoExamen.getText();
		String nombre = txtNombrePaciente.getText();
		double costo;
		try {
			costo = Double.parseDouble(txtCosto.getText().trim());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Ingrese un costo numérico válido.");
		}
		String tipo = cmbTipoSangre.getValue();
		if (tipo == null) {
			throw new IllegalArgumentException("Seleccione un tipo de sangre.");
		}
		FactorRh factorRh = rbPositivo.isSelected() ? FactorRh.POSITIVO : FactorRh.NEGATIVO;
		return new Examen(codigo, nombre, costo, TipoSangre.valueOf(tipo), factorRh);
	}

	private void mostrarExamen(Examen examen) {
		txtCodigoExamen.setText(examen.getCodigo());
		txtNombrePaciente.setText(examen.getNombrePaciente());
		txtCosto.setText(Double.toString(examen.getCosto()));
		cmbTipoSangre.setValue(examen.getTipoSangre().name());
		rbPositivo.setSelected(examen.getFactorRh() == FactorRh.POSITIVO);
		rbNegativo.setSelected(examen.getFactorRh() == FactorRh.NEGATIVO);
	}

	private String formatear(Examen examen) {
		String factor = examen.getFactorRh() == FactorRh.POSITIVO ? "Rh+" : "Rh-";
		return String.format("%s | %s | $%.2f | %s %s",
				examen.getCodigo(), examen.getNombrePaciente(), examen.getCosto(),
				examen.getTipoSangre(), factor);
	}

	private void mostrarMensaje(String mensaje) {
		txtAreaMensajes.setText(mensaje);
	}

	private void mostrarError(Exception error) {
		txtAreaMensajes.setText("Error: " + error.getMessage());
	}
}
