package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.*;
import java.net.Socket;


public class BuscarClasificacionController {

    @FXML
    private TextArea txA_ID;
    @FXML
    private TextArea txA_Name;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button btn_Buscar;

    private ObservableList<String> nombresClasificaciones;
    private ClasificacionXMLData clasificacionXMLData;

    private String serverIP;
    @FXML
    private BorderPane borderPane;

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
    public void BuscarOnAction(ActionEvent actionEvent) { // Ensure this matches the FXML file
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
            writer.flush();

            StringBuilder respuestaBuilder = new StringBuilder();
            String linea;
            int count = 0;
            while ((linea = reader.readLine()) != null && count <4 ) {
                respuestaBuilder.append(linea);
                count++;
            }
            String respuesta = respuestaBuilder.toString();

            if (respuesta != null && !respuesta.isEmpty()) {
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(new StringReader(respuesta));

                Element rootElement = document.getRootElement();
                String id = rootElement.getAttributeValue("IDclasificacion");
                String nombre = rootElement.getChildText("Name");

                mostrarClasificacion(id, nombre);
            } else {
                mostrarMensajeError("No se recibió una respuesta válida del servidor.");
            }
        } catch (IOException | JDOMException e) {
            mostrarMensajeError("Error de conexión o de procesamiento del servidor: " + e.getMessage());
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

}
