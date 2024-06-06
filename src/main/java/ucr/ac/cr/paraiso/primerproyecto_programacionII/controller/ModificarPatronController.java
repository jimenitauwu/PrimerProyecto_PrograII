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

    public void setPatronXMLData(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
        System.out.println("PatronXMLData set: " + patronXMLData);
        if (this.patronXMLData != null) {
            llenarComboBox();
        } else {
            mostrarMensajeError("Error: no se ha proporcionado el objeto PatronXMLData.");
        }
    }

    public void setClasificacionXMLData(ClasificacionXMLData clasificacionXMLData) {
        this.clasificacionXMLData = clasificacionXMLData;
        System.out.println("ClasificacionXMLData set: " + clasificacionXMLData);
        llenarComboBoxClasificaciones();
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
        txtID.setEditable(false);
        txtFieldNamePatron.setText(patron.getName());
        txtFieldProblema.setText(patron.getProblemaPatron());
        txtFieldContexto.setText(patron.getContextoPatron());
        txtFieldSolucion.setText(patron.getSolucionPatron());
        txtFieldEjemplos.setText(patron.getEjemplosPatron());
        txtClasificacionActual.setText(patron.getIdClasificacion());
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

        // Captura los valores de los campos
        String namePatron = txtFieldNamePatron.getText();
        String problema = txtFieldProblema.getText();
        String contexto = txtFieldContexto.getText();
        String solucion = txtFieldSolucion.getText();
        String ejemplos = txtFieldEjemplos.getText();
        String clasificacion = cBoxClasificacionModificada.getValue();

        // Crear un objeto Patron con los datos modificados
        Patron patronModificado = new Patron();

        if (!namePatron.isEmpty()) patronModificado.setName(namePatron);
        if (!problema.isEmpty()) patronModificado.setProblemaPatron(problema);
        if (!contexto.isEmpty()) patronModificado.setContextoPatron(contexto);
        if (!solucion.isEmpty()) patronModificado.setSolucionPatron(solucion);
        if (!ejemplos.isEmpty()) patronModificado.setEjemplosPatron(ejemplos);
        if (clasificacion != null) patronModificado.setIdClasificacion(clasificacion);

        // Enviar los datos modificados al servidor
        try {
            patronXMLData.modificarPatron(patronActual.getIdPatron(), patronModificado);

            // Comunicación con el servidor
            String patronXML = patronModificado.toXMLString();
            try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println("modificar");
                writer.println(patronXML);
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


