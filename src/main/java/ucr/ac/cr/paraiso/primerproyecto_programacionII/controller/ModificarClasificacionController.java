package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
public class ModificarClasificacionController {

    @FXML
    private ComboBox<String> cBox_ID;
    @FXML
    private TextField txt_ID;
    @FXML
    private TextField txt_Nombre;
    @FXML
    private Button btn_Modificar;
    @FXML
    private Button btn_Buscar;

    private ObservableList<String> nombresClasificaciones;
    private ClasificacionXMLData clasificacionXMLData;

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
                cBox_ID.setItems(nombresClasificaciones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar las clasificaciones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto ClasificacionXMLData.");
        }
    }

    @FXML
    public void buscarOnAction(ActionEvent actionEvent) {
        System.out.println("Buscar acción activada.");
        String seleccionado = cBox_ID.getValue();
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
        txt_ID.setText(id);
        txt_Nombre.setText(nombre);
    }

    @FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        System.out.println("Modificar acción activada.");
        String id = txt_ID.getText();
        String nombre = txt_Nombre.getText();
        if (id != null && !id.isEmpty() && nombre != null && !nombre.isEmpty()) {
            modificarClasificacion(id, nombre);
        } else {
            mostrarMensajeError("Por favor, ingrese un ID y un nombre de clasificación.");
        }
    }

    private void modificarClasificacion(String id, String nombre) {
        try (Socket socket = new Socket(serverIP, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(id + "\n" + nombre + "\n" + "modificar_clasificacion");
            String respuesta = reader.readLine();

            if (respuesta.startsWith("Error")) {
                mostrarMensajeError(respuesta);
            } else {
                mostrarMensajeExito("Clasificación modificada exitosamente.");
                llenarComboBox(); // Actualizar la lista de clasificaciones en el ComboBox
            }
        } catch (IOException e) {
            mostrarMensajeError("Error de conexión con el servidor.");
            e.printStackTrace();
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