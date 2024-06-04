package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;

import java.io.IOException;

public class BorrarClasificacionController
{
    private ClasificacionXMLData clasificacionXMLData; // Instancia de ClasificacionXMLData

    @javafx.fxml.FXML
    private Button btn_Borrar;

    @javafx.fxml.FXML
    private TextField textField_Eliminar;

    @javafx.fxml.FXML
    public void initialize() {
        try {
            clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void EliminarOnAction(ActionEvent actionEvent) {
        //Obtiene el ID de la clasificación a eliminar desde el campo de texto
        String idClasificacion = textField_Eliminar.getText().trim();

        //Verifica si el ID de la clasificación no está vacío
        if (!idClasificacion.isEmpty()) {
            try {
                clasificacionXMLData.eliminarClasificacion(idClasificacion);
                mostrarMensajeExito("Clasificación eliminada exitosamente.");
            } catch (IOException e) {
                mostrarMensajeError("Error al intentar eliminar la clasificación.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("Por favor, ingrese un ID de clasificación válido.");
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