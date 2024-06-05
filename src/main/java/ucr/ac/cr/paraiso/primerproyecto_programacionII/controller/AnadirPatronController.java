package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;


public class AnadirPatronController
{
    @javafx.fxml.FXML
    private ComboBox<String> cBoxClasificacion;
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
    private Button btnCancelar;
    @javafx.fxml.FXML
    private ImageView imgView;
    @javafx.fxml.FXML
    private Button btAñadir;
    @javafx.fxml.FXML
    private TextField txtFieldNamePatron;
    private ClasificacionXMLData clasificacionData;
    private PatronXMLData patronData;

    @javafx.fxml.FXML
    public void initialize() {
        try {
            // Inicializa los datos de clasificaciones
            clasificacionData = new ClasificacionXMLData("clasificaciones.xml");
            List<Clasificacion> clasificaciones = clasificacionData.obtenerClasificaciones();
            for (Clasificacion clasificacion : clasificaciones) {
                cBoxClasificacion.getItems().add(clasificacion.getNameClasificacion());
            }
            // Inicializa los datos de patrones
            patronData = new PatronXMLData("patrones.xml", clasificacionData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        // Limpia los campos del formulario
        txtFieldEjemplos.clear();
        txtFieldIDPatron.clear();
        txtFieldProblema.clear();
        txtFieldContexto.clear();
        txtFieldSolucion.clear();
        txtFieldNamePatron.clear();
        cBoxClasificacion.getSelectionModel().clearSelection();
    }

    @javafx.fxml.FXML
    public void anadirOnAction(ActionEvent actionEvent) {
        // Captura los valores de los campos
        String idPatron = txtFieldIDPatron.getText();
        String namePatron = txtFieldNamePatron.getText();
        String problema = txtFieldProblema.getText();
        String contexto = txtFieldContexto.getText();
        String solucion = txtFieldSolucion.getText();
        String ejemplos = txtFieldEjemplos.getText();
        String clasificacion = cBoxClasificacion.getValue();

        // Valida los datos ingresados
        if (idPatron.isEmpty() || namePatron.isEmpty() || problema.isEmpty() || contexto.isEmpty() || solucion.isEmpty() || clasificacion == null) {
            mostrarMensajeError("Todos los campos son obligatorios.");
            return;
        }

        // Verifica si el ID del patrón ya existe
        try {
            List<Patron> patrones = patronData.obtenerPatrones();
            for (Patron patron : patrones) {
                if (patron.getIdPatron().equals(idPatron)) {
                    mostrarMensajeError("El ID del patrón ya existe. Por favor, elige otro ID.");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error al verificar la existencia del ID del patrón.");
            return;
        }

        // Crea y envía el nuevo patrón al servidor
        Patron nuevoPatron = new Patron(idPatron, namePatron, problema, contexto, solucion, ejemplos, clasificacion);
        String patronXML = nuevoPatron.toXMLString();

        // Envía los datos al servidor para incluir un nuevo patrón
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(patronXML+"\n"+"incluir");//added JG
            String respuesta = reader.readLine();

            if (respuesta.contains("exitosamente")) {
                mostrarMensajeExito("Patrón añadido exitosamente.");
            } else {
                mostrarMensajeError("Error al añadir el patrón: " + respuesta);
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