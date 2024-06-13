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
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;



public class ModificarPatronController {
    @FXML
    private TextField txtFieldEjemplos;
    @FXML
    private TextField txtFieldProblema;
    @FXML
    private TextField txtFieldContexto;
    @FXML
    private TextField txtFieldSolucion;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtFieldNamePatron;
    @FXML
    private Button btnModificar;
    @FXML
    private TextArea txtClasificacionActual;
    @FXML
    private ComboBox<String> cBoxClasificacionModificada;
    @FXML
    private ImageView imgViewModificar;
    @FXML
    private ComboBox<String> cbBoxID;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtID;

    private ObservableList<String> nombresPatrones;
    private static Patron patronActual;
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    private String serverIP;

    public static void setPatronActual(Patron patron) {
        patronActual = patron;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setPatronXMLData(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
        llenarComboBox();
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        llenarComboBoxClasificaciones();
    }

    @FXML
    public void initialize() {
        nombresPatrones = FXCollections.observableArrayList();
        if (patronActual != null) {
            cargarDatosPatron(patronActual);
        }
    }

    private void llenarComboBox() {
        nombresPatrones.clear();
        if (patronXMLData != null) {
            try {
                for (Patron patron : patronXMLData.obtenerPatrones()) {
                    nombresPatrones.add(patron.getIdPatron() + " - " + patron.getName());
                }
                cbBoxID.setItems(nombresPatrones);
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar los patrones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto PatronXMLData.");
        }
    }

    @FXML
    public void BuscarOnAction(ActionEvent actionEvent) {
        String seleccionado = cbBoxID.getValue();
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

            writer.println(idPatron + "\n" + "consultar_patron_por_id");
            writer.flush();

            StringBuilder respuestaBuilder = new StringBuilder();
            String linea;
            int count = 0;
            while ((linea = reader.readLine()) != null && count<2) {//
                respuestaBuilder.append(linea);
                count++;
            }
            String respuesta = respuestaBuilder.toString();

            if (respuesta != null && !respuesta.isEmpty()) {
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(new StringReader(respuesta));

                Element rootElement = document.getRootElement();
                String nombre = rootElement.getChildText("Name");
                String problema = rootElement.getChildText("Problema");
                String clasificacion = rootElement.getChildText("Clasificacion");
                String solucion = rootElement.getChildText("Solucion");
                String contexto = rootElement.getChildText("Contexto");
                String ejemplos = rootElement.getChildText("Ejemplos");

                Patron patron = new Patron();
                patron.setIdPatron(idPatron);
                patron.setName(nombre);
                patron.setProblemaPatron(problema);
                patron.setContextoPatron(contexto);
                patron.setSolucionPatron(solucion);
                patron.setEjemplosPatron(ejemplos);
                patron.setIdClasificacion(clasificacion);

                patronActual = patron;
                cargarDatosPatron(patron);
            } else {
                mostrarMensajeError("No se recibió una respuesta válida del servidor.");
            }
        } catch (IOException | JDOMException e) {
            mostrarMensajeError("Error de conexión o de procesamiento del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosPatron(Patron patron) {
        txtID.setText(patron.getIdPatron());
        txtID.setEditable(false);
        txtFieldNamePatron.setText(patron.getName());
        txtFieldProblema.setText(patron.getProblemaPatron());
        txtFieldContexto.setText(patron.getContextoPatron());
        txtFieldSolucion.setText(patron.getSolucionPatron());
        txtFieldEjemplos.setText(patron.getEjemplosPatron());
        txtClasificacionActual.setText(clasificacionXMLData.obtenerIdClasificacionPorNombre(patron.getIdClasificacion()));
    }

    @FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtID.clear();
        txtFieldNamePatron.clear();
        txtFieldProblema.clear();
        txtFieldContexto.clear();
        txtFieldSolucion.clear();
        txtFieldEjemplos.clear();
        txtClasificacionActual.clear();
        cBoxClasificacionModificada.getSelectionModel().clearSelection();
    }

    @FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        if (patronActual == null) {
            mostrarMensajeError("No hay ningún patrón cargado para modificar.");
            return;
        }

        String namePatron = txtFieldNamePatron.getText();
        String idPatron = txtID.getText();
        String problema = txtFieldProblema.getText();
        String contexto = txtFieldContexto.getText();
        String solucion = txtFieldSolucion.getText();
        String ejemplos = txtFieldEjemplos.getText();
        String clasificacionActual = txtClasificacionActual.getText();
        String nuevaClasificacion = cBoxClasificacionModificada.getValue();

        // Obtén el ID de clasificación basado en el nombre seleccionado, si hay una nueva clasificación seleccionada
        String idClasificacion = (nuevaClasificacion != null && !nuevaClasificacion.isEmpty()) ?
                clasificacionXMLData.obtenerIdClasificacionPorNombre(nuevaClasificacion) : clasificacionActual;

        Patron patronModificado = new Patron();
        patronModificado.setIdPatron(patronActual.getIdPatron());

        if (!idPatron.isEmpty()) patronModificado.setIdPatron(idPatron);
        if (!namePatron.isEmpty()) patronModificado.setName(namePatron);
        if (!problema.isEmpty()) patronModificado.setProblemaPatron(problema);
        if (!contexto.isEmpty()) patronModificado.setContextoPatron(contexto);
        if (!solucion.isEmpty()) patronModificado.setSolucionPatron(solucion);
        if (!ejemplos.isEmpty()) patronModificado.setEjemplosPatron(ejemplos);
        if (idClasificacion != null) patronModificado.setIdClasificacion(idClasificacion);

        try {
            String patronXML = patronModificado.toXMLString();
            try (Socket socket = new Socket(serverIP, 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println(patronXML + "modificar" );
                writer.flush();
                String respuesta = reader.readLine();

                if (respuesta.contains("exitosamente")) {
                    mostrarMensajeExito("Patrón modificado exitosamente.");
                } else {
                    mostrarMensajeError("Error al modificar el patrón: " + respuesta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error de conexión con el servidor.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error al modificar el patrón.");
        }
    }

    private void llenarComboBoxClasificaciones() {
        if (clasificacionXMLData != null) {
            try {
                for (Clasificacion clasificacion : clasificacionXMLData.obtenerClasificaciones()) {
                    cBoxClasificacionModificada.getItems().add(clasificacion.getNameClasificacion());
                }
            } catch (Exception e) {
                mostrarMensajeError("Error al cargar las clasificaciones.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto ClasificacionXMLData.");
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
