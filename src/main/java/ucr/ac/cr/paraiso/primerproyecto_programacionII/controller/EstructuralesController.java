package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EstructuralesController {

    @FXML
    private TextArea txtNombre;
    @FXML
    private TextArea txtProblema;
    @FXML
    private TextArea txtClasificacion;
    @FXML
    private TextArea txtSolucion;
    @FXML
    private TextArea txtContexto;
    @FXML
    private TextArea txtEjemplos;
    @FXML
    private ImageView imgViewInfo;
    @FXML
    private Button btnBuscar;
    @FXML
    private ComboBox<String> cBoxID;

    private ObservableList<String> nombresPatrones;
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;
    private String serverIP;

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setPatronXMLData(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
        checkAndFillComboBox();
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        checkAndFillComboBox();
    }

    @FXML
    public void initialize() {
        nombresPatrones = FXCollections.observableArrayList();
    }

    private void checkAndFillComboBox() {
        if (this.patronXMLData != null && this.clasificacionXMLData != null) {
            llenarComboBox();
        } else {
            mostrarMensajeError("Error: no se ha proporcionado el objeto PatronXMLData o ClasificacionXMLData.");
        }
    }

    private void llenarComboBox() {
        nombresPatrones.clear(); // Asegurarse de empezar con una lista limpia
        try {
            String idClasificacionEstructural = obtenerIdClasificacionEstructural();
            if (idClasificacionEstructural != null) {
                for (Patron patron : patronXMLData.obtenerPatrones()) {
                    if (patron.getIdClasificacion().equals(idClasificacionEstructural)) {
                        nombresPatrones.add(patron.getIdPatron() + " - " + patron.getName());
                    }
                }
                cBoxID.setItems(nombresPatrones);
            } else {
                mostrarMensajeError("Clasificación 'Estructural' no encontrada.");
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al cargar los patrones.");
            e.printStackTrace();
        }
    }

    private String obtenerIdClasificacionEstructural() {
        try {
            for (Clasificacion clasificacion : clasificacionXMLData.obtenerClasificaciones()) {
                if ("Estructural".equals(clasificacion.getNameClasificacion())) {
                    return clasificacion.getIdClasificacion();
                }
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al obtener la clasificación 'Estructural'.");
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    public void buscarOnAction(ActionEvent actionEvent) {
        System.out.println("Buscar acción activada.");
        String seleccionado = cBoxID.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idPatron = partes[0];  // Asumiendo que el ID está antes del guion
            buscarPatron(idPatron);
        } else {
            mostrarMensajeError("Por favor, seleccione un patrón.");
        }
    }

    private void buscarPatron(String idPatron) {
        try (Socket socket = new Socket(serverIP, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(idPatron + "\n" + "consultar_patron_por_id");
            String respuesta = reader.readLine();

            if (respuesta.startsWith("Error")) {
                mostrarMensajeError(respuesta);
            } else {
                String[] partes = respuesta.split(",");
                String nombre = partes[0];
                String problema = partes[1];
                String clasificacion = partes[2];
                String solucion = partes[3];
                String contexto = partes[4];
                String ejemplos = partes[5];
                mostrarPatron(nombre, problema, clasificacion, solucion, contexto, ejemplos);
            }
        } catch (IOException e) {
            mostrarMensajeError("Error de conexión con el servidor.");
            e.printStackTrace();
        }
    }

    private void mostrarPatron(String nombre, String problema, String clasificacion, String solucion, String contexto, String ejemplos) {
        txtNombre.setText(nombre);
        txtProblema.setText(problema);
        txtContexto.setText(contexto);
        txtSolucion.setText(solucion);
        txtEjemplos.setText(ejemplos);
        txtClasificacion.setText(clasificacion);
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

