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
        nombresPatrones.clear(); // Asegurarse de empezar con una lista limpia
        if (patronXMLData != null && clasificacionXMLData != null) {
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
        } else {
            mostrarMensajeError("No se ha proporcionado el objeto PatronXMLData o ClasificacionXMLData.");
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
