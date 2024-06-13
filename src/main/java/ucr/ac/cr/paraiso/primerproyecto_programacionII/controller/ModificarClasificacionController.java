package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;


public class ModificarClasificacionController {

    @FXML
    private ComboBox<String> cBox_ID;

    private ObservableList<String> nombresClasificaciones;
    private ClasificacionXMLData clasificacionXMLData;

    private String serverIP;

    private static Clasificacion clasificacionActual;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;

    public static void setClasificacionActual(Clasificacion clasificacion) {
        clasificacionActual = clasificacion;
    }

    public static Clasificacion getClasificacionActual() {
        return clasificacionActual;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        llenarComboBox();
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
            writer.flush();

            StringBuilder respuestaBuilder = new StringBuilder();
            String linea;
            int count = 0;
            while ((linea = reader.readLine()) != null && count < 4) {
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

                // Configurar clasificacionActual
                clasificacionActual = new Clasificacion(id, nombre);
                cargarDatosClasificacion(id, nombre);
            } else {
                mostrarMensajeError("No se recibió una respuesta válida del servidor.");
            }
        } catch (IOException | JDOMException e) {
            mostrarMensajeError("Error de conexión o de procesamiento del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void cargarDatosClasificacion(String id, String nombre) {
        txtID.setText(id);
        txtName.setText(nombre);

        // Configurar clasificacionActual
        clasificacionActual = new Clasificacion(id, nombre);
    }


    @FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        if (clasificacionActual == null) {
            mostrarMensajeError("No hay ninguna clasificación cargada para modificar.");
            return;
        }

        String id = txtID.getText();
        String nombre = txtName.getText();


        Clasificacion clasificacionModificada = new Clasificacion(clasificacionActual.getIdClasificacion(), nombre);

        try {
            String clasificacionXML = clasificacionModificada.toXMLString();
            try (Socket socket = new Socket(serverIP, 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println(clasificacionXML + "\nmodificar_clasificacion");
                writer.flush();
                String respuesta = reader.readLine();


                if (respuesta != null && respuesta.contains("exitosamente")) {
                    mostrarMensajeExito("Clasificación modificada exitosamente.");
                    llenarComboBox(); // Actualizar la lista de clasificaciones en el ComboBox
                } else {
                    mostrarMensajeError("Error al modificar la clasificación: " + respuesta);
                }
            } catch (IOException e) {
                mostrarMensajeError("Error de conexión con el servidor: " + e.getMessage());
                e.printStackTrace();
            }
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