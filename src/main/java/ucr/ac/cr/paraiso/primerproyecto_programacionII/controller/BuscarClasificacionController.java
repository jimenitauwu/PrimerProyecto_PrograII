package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;

public class BuscarClasificacionController
{
    @javafx.fxml.FXML
    private BorderPane borderPane;
    @javafx.fxml.FXML
    private TextArea txA_ID;
    @javafx.fxml.FXML
    private TextArea txA_Name;
    @javafx.fxml.FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button btn_Buscar;


    Clasificacion c= new Clasificacion();
    private ClasificacionXMLData clasificacionXMLData;
    private ObservableList<String> nombresClasificaciones;


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
        nombresClasificaciones.clear();  // Asegurarse de empezar con una lista limpia
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
    public void BuscarOnAction(ActionEvent actionEvent) {
        System.out.println("Buscar acción activada.");
        String seleccionado = comboBox.getValue();
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
            Clasificacion clasificacion = clasificacionXMLData.obtenerClasificacionPorId(idPatron);
            if (clasificacion != null) {
                mostrarClasificacion(clasificacion);
            } else {
                mostrarMensajeError("Patrón no encontrado.");
            }
        } catch (Exception e) {
            mostrarMensajeError("Error al buscar el patrón.");
            e.printStackTrace();
        }
    }

    private void mostrarClasificacion(Clasificacion clasificacion) {
        txA_ID.setText(c.getIdClasificacion());
        txA_Name.setText(c.getNameClasificacion());

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