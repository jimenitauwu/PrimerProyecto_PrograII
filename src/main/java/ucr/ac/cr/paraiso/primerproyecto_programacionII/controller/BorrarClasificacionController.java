package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BorrarClasificacionController {
    @FXML
    private ComboBox<String> cbBoxClasificacion;
    @FXML
    private Button btn_Borrar;

    private ClasificacionXMLData clasificacionXMLData;
    private ObservableList<String> nombresClasificaciones;

    // Añadir un campo para la IP del servidor
    private String serverIP;

    // Crear un método para establecer la IP del servidor
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        if (this.clasificacionXMLData != null) {
            llenarComboBox();
        } else {
            mostrarMensajeError("Error: no se ha proporcionado el objeto ClasificacionXMLData.");
        }
    }

    @FXML
    public void initialize() {
        nombresClasificaciones = FXCollections.observableArrayList();
    }

    private void llenarComboBox() {
        nombresClasificaciones.clear();  // Asegurarse de empezar con una lista limpia
        if (clasificacionXMLData != null) {
            try {
                for (Clasificacion clasificacion : clasificacionXMLData.obtenerClasificaciones()) {
                    String nombreClasificacion = clasificacion.getIdClasificacion() + " - " + clasificacion.getNameClasificacion();
                    nombresClasificaciones.add(nombreClasificacion);
                }
                cbBoxClasificacion.setItems(nombresClasificaciones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar las clasificaciones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto ClasificacionXMLData.");
        }
    }

    @FXML
    public void EliminarOnAction(ActionEvent actionEvent) {
        String seleccionado = cbBoxClasificacion.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idClasificacion = partes[0];  // Asumiendo que el ID está antes del guion
            try (Socket socket = new Socket(serverIP, 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println(idClasificacion + "\n" + "eliminar_clasificacion");
                String respuesta = reader.readLine();

                if (respuesta.contains("exitosamente")) {
                    mostrarMensajeExito("Clasificación eliminada exitosamente.");
                    llenarComboBox(); // Actualiza el ComboBox después de eliminar la clasificación
                } else {
                    mostrarMensajeError("Error al eliminar la clasificación: " + respuesta);
                }
            } catch (IOException e) {
                mostrarMensajeError("Error de conexión con el servidor.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("Por favor, seleccione una clasificación.");
        }
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
