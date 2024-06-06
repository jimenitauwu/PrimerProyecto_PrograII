package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BuscarClasificacionController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextArea txA_ID;
    @FXML
    private TextArea txA_Name;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button btn_Buscar;

    private ClasificacionXMLData clasificacionXMLData;
    private ObservableList<String> nombresClasificaciones;

    private String serverIP;

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
        nombresClasificaciones.clear();
        if (clasificacionXMLData != null) {
            try {
                for (Clasificacion clasificacion : clasificacionXMLData.obtenerClasificaciones()) {
                    String nombreClasificacion = clasificacion.getIdClasificacion() + " - " + clasificacion.getNameClasificacion();
                    nombresClasificaciones.add(nombreClasificacion);
                }
                comboBox.setItems(nombresClasificaciones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar las clasificaciones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto ClasificacionXMLData.");
        }
    }

    @FXML
    public void BuscarOnAction(ActionEvent actionEvent) {
        System.out.println("Buscar acción activada.");
        String seleccionado = comboBox.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idClasificacion = partes[0];
            buscarClasificacion(idClasificacion);
        } else {
            mostrarMensajeError("Por favor, seleccione una clasificación.");
        }
    }

    private void buscarClasificacion(String idClasificacion) {
        try (Socket socket = new Socket(serverIP, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(idClasificacion + "\n" + "consultar_clasificacion_por_id");
            String respuesta = reader.readLine();

            if (respuesta.startsWith("Error")) {
                mostrarMensajeError(respuesta);
            } else {
                String[] partes = respuesta.split(",");
                String id = partes[0];
                String nombre = partes[1];
                mostrarClasificacion(id, nombre);
            }
        } catch (IOException e) {
            mostrarMensajeError("Error de conexión con el servidor.");
            e.printStackTrace();
        }
    }

    private void mostrarClasificacion(String id, String nombre) {
        txA_ID.setText(id);
        txA_Name.setText(nombre);
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