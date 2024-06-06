package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
public class AnadirClasificacionController {

    @javafx.fxml.FXML
    private TextField txtFieldNomClasificacion;
    @javafx.fxml.FXML
    private TextField txtFieldIDClasificacion;

    private ClasificacionXMLData clasificacionData;

    // Añadir un campo para la IP del servidor
    private String serverIP;

    // Crear un método para establecer la IP del servidor
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @javafx.fxml.FXML
    public void initialize() {
        try {
            // Inicializa los datos de clasificaciones
            clasificacionData = new ClasificacionXMLData("clasificaciones.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        // Limpia los campos del formulario
        txtFieldNomClasificacion.clear();
        txtFieldIDClasificacion.clear();
    }

    @javafx.fxml.FXML
    public void anadirOnAction(ActionEvent actionEvent) {
        // Captura los valores de los campos
        String idClasificacion = txtFieldIDClasificacion.getText();
        String nombreClasificacion = txtFieldNomClasificacion.getText();

        // Valida los datos ingresados
        if (idClasificacion.isEmpty() || nombreClasificacion.isEmpty()) {
            mostrarMensajeError("Todos los campos son obligatorios.");
            return;
        }

        // Verifica si el ID de la clasificación ya existe
        try {
            List<Clasificacion> clasificaciones = clasificacionData.obtenerClasificaciones();
            for (Clasificacion clasificacion : clasificaciones) {
                if (clasificacion.getIdClasificacion().equals(idClasificacion)) {
                    mostrarMensajeError("El ID de la clasificación ya existe. Por favor, elige otro ID.");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error al verificar la existencia del ID de la clasificación.");
            return;
        }

        // Crea y envía la nueva clasificación al servidor
        Clasificacion nuevaClasificacion = new Clasificacion(idClasificacion, nombreClasificacion);
        String clasificacionXML = nuevaClasificacion.toXMLString();

        // Envía los datos al servidor para incluir una nueva clasificación
        try (Socket socket = new Socket(serverIP, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(clasificacionXML + "\n" + "incluir_clasificacion");
            String respuesta = reader.readLine();

            if (respuesta.contains("exitosamente")) {
                mostrarMensajeExito("Clasificación añadida exitosamente.");
            } else {
                mostrarMensajeError("Error al añadir la clasificación: " + respuesta);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error de conexión con el servidor.");
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
