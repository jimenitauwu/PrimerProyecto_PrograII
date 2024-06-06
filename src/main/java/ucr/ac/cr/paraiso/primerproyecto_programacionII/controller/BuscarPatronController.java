package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.*;
import java.net.Socket;

public class BuscarPatronController {
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
        nombresPatrones.clear();
        if (patronXMLData != null) {
            try {
                for (Patron patron : patronXMLData.obtenerPatrones()) {
                    nombresPatrones.add(patron.getIdPatron() + " - " + patron.getName());
                }
                cBoxID.setItems(nombresPatrones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar los patrones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto PatronXMLData.");
        }
    }

    @FXML
    public void buscarOnAction(ActionEvent actionEvent) {
        String seleccionado = cBoxID.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idPatron = partes[0];
            buscarPatron(idPatron);
        } else {
            mostrarMensajeError("Por favor, seleccione un patrón.");
        }
    }

    private void buscarPatron(String idPatron) {
        try (Socket socket = new Socket(serverIP, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the request to the server
            writer.println(idPatron);
            writer.println("consultar_patron_por_id");
            writer.flush();

            // Read the response from the server
            StringBuilder respuestaBuilder = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                respuestaBuilder.append(linea);
            }
            String respuesta = respuestaBuilder.toString();

            // Process the server's response
            if (respuesta != null && !respuesta.isEmpty()) {
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(new StringReader(respuesta));

                Element rootElement = document.getRootElement();

                String nombre = rootElement.getChildText("name");
                String problema = rootElement.getChildText("problemaPatron");
                String clasificacion = rootElement.getChildText("clasificacion");
                String solucion = rootElement.getChildText("solucionPatron");
                String contexto = rootElement.getChildText("contextoPatron");
                String ejemplos = rootElement.getChildText("ejemplosPatron");

                mostrarPatron(nombre, problema, clasificacion, solucion, contexto, ejemplos);
            } else {
                mostrarMensajeError("No se recibió una respuesta válida del servidor.");
            }
        } catch (IOException | JDOMException e) {
            mostrarMensajeError("Error de conexión o de procesamiento del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarPatron(String nombre, String problema, String clasificacion, String solucion, String contexto, String ejemplos) {
        txtNombre.setText(nombre);
        txtProblema.setText(problema);
        txtClasificacion.setText(clasificacion);
        txtSolucion.setText(solucion);
        txtContexto.setText(contexto);
        txtEjemplos.setText(ejemplos);
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


