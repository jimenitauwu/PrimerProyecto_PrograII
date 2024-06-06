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
    private TextField txtFieldNomClasificacion;
    @FXML
    private TextField txtFieldIDClasificacion;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ComboBox<String> cbBoxClasif;

    private ObservableList<String> clasificaciones;
    private Clasificacion clasificacionActual;
    private ClasificacionXMLData clasificacionXMLData;

    // Método para establecer el objeto ClasificacionXMLData
    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        llenarComboBoxClasificaciones();
    }


    @FXML
    public void initialize() {
        clasificaciones = FXCollections.observableArrayList();
    }

    private void llenarComboBoxClasificaciones() {
        if (clasificacionXMLData != null) {
            try {
                for (Clasificacion clasificacion : clasificacionXMLData.obtenerClasificaciones()) {
                    cbBoxClasif.getItems().add(clasificacion.getIdClasificacion() + " - " + clasificacion.getNameClasificacion());
                }
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
        String seleccionado = cbBoxClasif.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idClasificacion = partes[0];  // Asumiendo que el ID está antes del guion
            buscarClasificacion(idClasificacion);
        } else {
            mostrarMensajeError("Por favor, seleccione una Clasificación.");
        }
    }

    private void buscarClasificacion(String idClasificacion) {
        try {
            Clasificacion clasificacion = clasificacionXMLData.obtenerClasificacionPorId(idClasificacion);
            if (clasificacion != null) {
                clasificacionActual = clasificacion;
                mostrarClasificacion(clasificacion);
            } else {
                mostrarMensajeError("Clasificación no encontrada.");
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al buscar la clasificación.");
            e.printStackTrace();
        }
    }

    private void mostrarClasificacion(Clasificacion clasificacion) {
        txtFieldIDClasificacion.setText(clasificacion.getIdClasificacion()); // Mostrar el ID actual
        txtFieldIDClasificacion.setEditable(false);
        txtFieldNomClasificacion.setText(clasificacion.getNameClasificacion());
    }

    @FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        if (clasificacionActual == null) {
            mostrarMensajeError("No hay ninguna clasificación cargada para modificar.");
            return;
        }

        // Captura el valor del nombre de la clasificación
        String nombreClasificacion = txtFieldNomClasificacion.getText();

        // Crear un objeto Clasificacion con el nombre modificado
        Clasificacion clasificacionModificada = new Clasificacion();
        clasificacionModificada.setIdClasificacion(clasificacionActual.getIdClasificacion());
        if (!nombreClasificacion.isEmpty()) {
            clasificacionModificada.setNameClasificacion(nombreClasificacion);
        }

        // Enviar los datos modificados al servidor
        try {
            clasificacionXMLData.modificarClasificacion(clasificacionActual.getIdClasificacion(), clasificacionModificada);

            // Comunicación con el servidor
            String clasficacionXML = clasificacionModificada.toXMLString();
            try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println("modificar_clasificacion");
                writer.println(clasficacionXML);
                String respuesta = reader.readLine();

                if (respuesta.contains("exitosamente")) {
                    mostrarMensajeExito("Clasificación modificado exitosamente.");
                } else {
                    mostrarMensajeError("Error al modificar la Clasificación: " + respuesta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error de conexión con el servidor.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error al modificar la clasificación.");
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