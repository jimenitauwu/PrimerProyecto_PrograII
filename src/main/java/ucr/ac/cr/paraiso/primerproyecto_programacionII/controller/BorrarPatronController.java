package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;

import java.io.IOException;

public class BorrarPatronController
{
    private PatronXMLData patronXMLData; // Instancia de PatronXMLData

    @javafx.fxml.FXML
    private Button btn_Borrar;

    @javafx.fxml.FXML
    private TextField textField_Eliminar;

    @javafx.fxml.FXML
    public void initialize() {
        try {
            patronXMLData = new PatronXMLData("patrones.xml", new ClasificacionXMLData("clasificaciones.xml"));
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void EliminarOnAction(ActionEvent actionEvent) {
        //Obtiene el ID del patrón a eliminar desde el campo de texto
        String idPatron = textField_Eliminar.getText().trim();

        //Verifica si el ID del patrón no está vacío
        if (!idPatron.isEmpty()) {
            try {
                patronXMLData.eliminarPatron(idPatron);
                mostrarMensajeExito("Patrón eliminado exitosamente.");
            } catch (IOException e) {
                mostrarMensajeError("Error al intentar eliminar el patrón.");
                e.printStackTrace();
            }
        } else {
            mostrarMensajeError("Por favor, ingrese un ID de patrón válido.");
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