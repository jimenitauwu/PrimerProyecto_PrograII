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
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

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
        // El ComboBox se llenará cuando setPatronXMLData sea llamado
    }

    private void llenarComboBox() {
        nombresPatrones.clear();  // Asegurarse de empezar con una lista limpia
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
        txtNombre.setText(patron.getName());
        txtProblema.setText(patron.getProblemaPatron());
        txtContexto.setText(patron.getContextoPatron());
        txtSolucion.setText(patron.getSolucionPatron());
        txtEjemplos.setText(patron.getEjemplosPatron());
        txtClasificacion.setText(patron.getIdClasificacion());
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}