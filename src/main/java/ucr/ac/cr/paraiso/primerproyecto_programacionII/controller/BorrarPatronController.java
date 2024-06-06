package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
public class BorrarPatronController {
    @FXML
    private ComboBox<String> cbBoxPatron;
    @FXML
    private Button btn_Borrar;

    private PatronXMLData patronXMLData;
    private ObservableList<String> nombresPatrones;
    private ClasificacionXMLData clasificacionXMLData;

    // Añadir un campo para la IP del servidor
    private String serverIP;

    // Crear un método para establecer la IP del servidor
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setPatronXMLData(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
        if (this.patronXMLData != null) {
            llenarComboBox();
        } else {
            mostrarMensajeError("Error: no se ha proporcionado el objeto PatronXMLData.");
        }
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
    }

    @FXML
    public void initialize() {
        nombresPatrones = FXCollections.observableArrayList();
    }

    private void llenarComboBox() {
        nombresPatrones.clear();  // Asegurarse de empezar con una lista limpia
        if (patronXMLData != null) {
            try {
                for (Patron patron : patronXMLData.obtenerPatrones()) {
                    String nombrePatron = patron.getIdPatron() + " - " + patron.getName();
                    nombresPatrones.add(nombrePatron);
                }
                cbBoxPatron.setItems(nombresPatrones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar los patrones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto PatronXMLData.");
        }
    }

    @FXML
    public void EliminarOnAction(ActionEvent actionEvent) {
        String seleccionado = cbBoxPatron.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idPatron = partes[0];  // Asumiendo que el ID está antes del guion
            try (Socket socket = new Socket(serverIP, 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println(idPatron + "\n" + "eliminar");
                String respuesta = reader.readLine();

                if (respuesta.contains("exitosamente")) {
                    mostrarMensajeExito("Patrón eliminado exitosamente.");
                    llenarComboBox(); // Actualiza el ComboBox después de eliminar el patrón
                } else {
                    mostrarMensajeError("Error al eliminar el patrón: " + respuesta);
                }
            } catch (IOException e) {
                mostrarMensajeError("Error de conexión con el servidor.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("Por favor, seleccione un patrón.");
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
