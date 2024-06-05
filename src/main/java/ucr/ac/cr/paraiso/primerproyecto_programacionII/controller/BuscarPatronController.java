package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.PatronModelo;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class BuscarPatronController
{
    @javafx.fxml.FXML
    private TextField textField_Buscar;
    @javafx.fxml.FXML
    private BorderPane borderPane;
    @javafx.fxml.FXML
    private Button btn_Informacion;
    @javafx.fxml.FXML
    private Button btnModificar;

    private PatronModelo patronModelo;

    public void setPatronModelo(PatronModelo patronModelo) {
        this.patronModelo = patronModelo;
    }

    private Patron patronBuscado;

    @javafx.fxml.FXML
    public void initialize() {
        patronModelo = new PatronModelo();
    }

    @javafx.fxml.FXML
    public void informacionOnAction(ActionEvent actionEvent) {
        String idPatron = textField_Buscar.getText();
        buscarPatron(idPatron);
        if (patronBuscado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("informacionPatron.fxml"));
                Parent root = loader.load();
                InformacionPatronController controller = loader.getController();
                controller.setPatronModelo(patronModelo);
                controller.setPatron(patronBuscado);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarMensajeError("Error al cargar la ventana de información del patrón.");
            }
        }
    }

    @javafx.fxml.FXML
    public void ModificarOnAction(ActionEvent actionEvent) {
        String idPatron = textField_Buscar.getText();
        buscarPatron(idPatron);
        if (patronBuscado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("modificarPatron.fxml"));
                Parent root = loader.load();
                ModificarPatronController controller = loader.getController();
                controller.setPatronModelo(patronModelo);
                controller.setPatron(patronBuscado);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarMensajeError("Error al cargar la ventana de modificación del patrón.");
            }
        }
    }




    // Método para buscar el patrón por su ID
    private void buscarPatron(String idPatron) {
        if (idPatron.isEmpty()) {
            mostrarMensajeError("El campo de búsqueda no puede estar vacío.");
            return;
        }

        // Realiza la búsqueda del patrón por ID en el servidor
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(idPatron + "\n" + "consultar");
            String respuesta = reader.readLine();

            if (respuesta.contains("no encontrado")) {
                mostrarMensajeError("Patrón no encontrado.");
                patronBuscado = null; // Asegúrate de que patronBuscado sea null si no se encuentra el patrón
            } else {
                patronBuscado = Patron.fromXMLString(respuesta);
                patronModelo.setPatronBuscado(patronBuscado); // Almacena el patrón buscado en el modelo
                mostrarMensajeExito("Patrón encontrado.");
            }
        } catch (IOException | JDOMException e) {
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