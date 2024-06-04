package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class anadirClasificacionController
{

    @javafx.fxml.FXML
    private TextField txtFieldNomClasificacion;
    @javafx.fxml.FXML
    private TextField txtFieldIDClasifiacion;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        //Limpia los campos del formulario
        txtFieldNomClasificacion.clear();
        txtFieldIDClasifiacion.clear();
    }

    @javafx.fxml.FXML
    public void anadirOnAction(ActionEvent actionEvent) {
        //Captura los valores de los campos
        String idClasificacion = txtFieldIDClasifiacion.getText();
        String nombreClasificacion = txtFieldNomClasificacion.getText();

        //Valida los datos ingresados
        if (idClasificacion.isEmpty() || nombreClasificacion.isEmpty()) {
            mostrarMensajeError("Todos los campos son obligatorios.");
            return;
        }

        Clasificacion nuevaClasificacion = new Clasificacion(idClasificacion, nombreClasificacion);
        String clasificacionXML = nuevaClasificacion.toXMLString();

        //Envia los datos al servidor para incluir una nueva clasificación
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("incluir " + clasificacionXML);
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