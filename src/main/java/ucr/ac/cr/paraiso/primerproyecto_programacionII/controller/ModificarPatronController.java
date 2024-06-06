package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.jdom2.JDOMException;
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
    private Patron patronActual;
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    // Método para establecer la instancia PatronXMLData
    public void setPatronXMLData(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
        if (this.patronXMLData != null) {
            llenarComboBox();
        } else {
            mostrarMensajeError("Error: no se ha proporcionado el objeto PatronXMLData.");
        }
    }

    // Método para establecer la instancia ClasificacionXMLData
    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        llenarComboBoxClasificaciones();
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

    @FXML
    public void BuscarOnAction(ActionEvent actionEvent) {
        System.out.println("Buscar acción activada.");
        String seleccionado = cbBoxID.getValue();
        if (seleccionado != null) {
            String[] partes = seleccionado.split(" - ");
            String idPatron = partes[0];  // Asumiendo que el ID está antes del guion
            buscarPatron(idPatron);
        } else {
            mostrarMensajeError("Por favor, seleccione un patrón.");
        }
    }

    private void buscarPatron(String idPatron) {
        try {
            Patron patron = patronXMLData.obtenerPatronPorID(idPatron);
            if (patron != null) {
                patronActual = patron;
                mostrarPatron(patron);
            } else {
                mostrarMensajeError("Patrón no encontrado.");
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al buscar el patrón.");
            e.printStackTrace();
        }
    }

    private void mostrarPatron(Patron patron) {
        txtID.setText(patron.getIdPatron()); // Mostrar el ID actual
        txtFieldNamePatron.setText(patron.getName());
        txtFieldProblema.setText(patron.getProblemaPatron());
        txtFieldContexto.setText(patron.getContextoPatron());
        txtFieldSolucion.setText(patron.getSolucionPatron());
        txtFieldEjemplos.setText(patron.getEjemplosPatron());
        txtClasificacionActual.setText(patron.getIdClasificacion());
    }

    @FXML
    public void cancelarOnAction(ActionEvent actionEvent) {
        txtID.clear();
        txtFieldNamePatron.clear();
        txtFieldProblema.clear();
        txtFieldContexto.clear();
        txtFieldSolucion.clear();
        txtFieldEjemplos.clear();
        cBoxClasificacionModificada.getSelectionModel().clearSelection();
    }

    @FXML
    public void modificarOnAction(ActionEvent actionEvent) {
        if (patronActual == null) {
            mostrarMensajeError("No hay ningún patrón cargado para modificar.");
            return;
        }

        patronActual.setIdPatron(txtID.getText());
        patronActual.setName(txtFieldNamePatron.getText());
        patronActual.setProblemaPatron(txtFieldProblema.getText());
        patronActual.setContextoPatron(txtFieldContexto.getText());
        patronActual.setSolucionPatron(txtFieldSolucion.getText());
        patronActual.setEjemplosPatron(txtFieldEjemplos.getText());
        patronActual.setIdClasificacion(cBoxClasificacionModificada.getValue());

        System.out.println("Modificando patrón: " + patronActual.toXMLString()); // Agrega este mensaje para depuración

        try {
            // Envía la solicitud al servidor para modificar el patrón
            enviarModificacionAlServidor(patronActual);
            mostrarMensajeExito("Patrón modificado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensajeError("Error al modificar el patrón.");
        }
    }

    private void enviarModificacionAlServidor(Patron patron) throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        try (Socket socket = new Socket(inetAddress, 9999);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String comando = "modificar";
            String xmlData = patron.toXMLString();

            System.out.println("Enviando comando: " + comando);
            System.out.println("Enviando datos del patrón: " + xmlData);

            writer.println(comando);
            writer.println(xmlData);

            String respuesta = reader.readLine();
            System.out.println("Respuesta del servidor: " + respuesta);
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



