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
        System.out.println("Buscar acción activada.");
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
        System.out.println("Conectando al servidor en " + serverIP + " en el puerto 9999...");
        try (Socket socket = new Socket(serverIP, 9999)) {
            System.out.println("Conexión establecida correctamente.");

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Envía la solicitud al servidor
            writer.println(idPatron + "\n" + "consultar_patron_por_id");
            System.out.println("Solicitud enviada al servidor.");

            // Lee la respuesta del servidor
            StringBuilder respuestaBuilder = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                respuestaBuilder.append(linea);
            }
            String respuesta = respuestaBuilder.toString();
            System.out.println("Respuesta recibida del servidor: " + respuesta);

            // Procesa la respuesta del servidor
            if (respuesta != null && !respuesta.isEmpty()) {
                // Convierte la respuesta en un documento XML
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(new StringReader(respuesta));

                // Obtén la raíz del documento
                Element rootElement = document.getRootElement();

                // Extrae los datos del patrón
                String nombre = rootElement.getChildText("nombre");
                String problema = rootElement.getChildText("problema");
                String clasificacion = rootElement.getChildText("clasificacion");
                String solucion = rootElement.getChildText("solucion");
                String contexto = rootElement.getChildText("contexto");
                String ejemplos = rootElement.getChildText("ejemplos");

                // Muestra los datos en los TextArea
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


