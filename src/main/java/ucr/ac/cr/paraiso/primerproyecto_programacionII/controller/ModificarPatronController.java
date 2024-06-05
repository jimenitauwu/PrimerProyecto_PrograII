package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.PatronModelo;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ModificarPatronController
{
    @javafx.fxml.FXML
    private TextField txtFieldEjemplos;
    @javafx.fxml.FXML
    private TextField txtFieldIDPatron;
    @javafx.fxml.FXML
    private TextField txtFieldProblema;
    @javafx.fxml.FXML
    private TextField txtFieldContexto;
    @javafx.fxml.FXML
    private TextField txtFieldSolucion;
    @javafx.fxml.FXML
    private ComboBox<String> cBoxClasificacion;
    @javafx.fxml.FXML
    private Button btnCancelar;
    @javafx.fxml.FXML
    private ImageView imgView;
    @javafx.fxml.FXML
    private TextField txtFieldNamePatron;
    @javafx.fxml.FXML
    private Button btnModificar;

    private PatronModelo patronModelo;

    public void setPatronModelo(PatronModelo patronModelo) {
        this.patronModelo = patronModelo;
    }

    public void setPatron(Patron patron) {
        // Si el patrón se almacena en un objeto PatronModelo, puedes hacer la conversión aquí
        PatronModelo modelo = new PatronModelo(patron.getIdPatron(), patron.getName(), patron.getProblemaPatron(),
                patron.getContextoPatron(), patron.getSolucionPatron(),
                patron.getEjemplosPatron(), patron.getIdClasificacion());
        setPatronModelo(modelo);
    }


    @javafx.fxml.FXML
    public void initialize() {
    }


    @javafx.fxml.FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        txtFieldIDPatron.clear();
        txtFieldNamePatron.clear();
        txtFieldProblema.clear();
        txtFieldContexto.clear();
        txtFieldSolucion.clear();
        txtFieldEjemplos.clear();
        cBoxClasificacion.getSelectionModel().clearSelection();
    }

    @javafx.fxml.FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        // Actualiza los valores del patrón en el modelo
        patronModelo.setName(txtFieldNamePatron.getText());
        patronModelo.setProblemaPatron(txtFieldProblema.getText());
        patronModelo.setContextoPatron(txtFieldContexto.getText());
        patronModelo.setSolucionPatron(txtFieldSolucion.getText());
        patronModelo.setEjemplosPatron(txtFieldEjemplos.getText());
        patronModelo.setIdClasificacion(cBoxClasificacion.getValue());

        // Convierte el patrón a XML
        String patronXML = patronModelo.toXMLString();

        // Envía los datos al servidor para modificar el patrón
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(patronXML + "\n" + "modificar");
            String respuesta = reader.readLine();

            if (respuesta.contains("exitosamente")) {
                mostrarMensajeExito("Patrón modificado exitosamente.");
            } else {
                mostrarMensajeError("Error al modificar el patrón: " + respuesta);
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