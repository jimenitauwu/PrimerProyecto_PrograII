package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;
import java.util.List;

public class PruebaController {
    @FXML
    private ComboBox<String> patronComboBox;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField contextoTextField;
    @FXML
    private TextField problemaTextField;
    @FXML
    private TextField solucionTextField;
    @FXML
    private TextField ejemplosTextField;
    @FXML
    private TextField clasificacionTextField;

    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    public void initialize() {
        try {
            clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
            patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);
            cargarPatronesEnComboBox();
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }

    private void cargarPatronesEnComboBox() {
        List<Patron> patrones = patronXMLData.obtenerPatrones();
        for (Patron patron : patrones) {
            patronComboBox.getItems().add(patron.getIdPatron() + " - " + patron.getName());
        }
    }

    @FXML
    private void cargarPatronSeleccionado() {
        String selectedPatron = patronComboBox.getSelectionModel().getSelectedItem();
        if (selectedPatron != null) {
            String idPatron = selectedPatron.split(" - ")[0];
            Patron patron = patronXMLData.obtenerPatronPorID(idPatron);

            if (patron != null) {
                idTextField.setText(patron.getIdPatron());
                nameTextField.setText(patron.getName());
                contextoTextField.setText(patron.getContextoPatron());
                problemaTextField.setText(patron.getProblemaPatron());
                solucionTextField.setText(patron.getSolucionPatron());
                ejemplosTextField.setText(patron.getEjemplosPatron());
                clasificacionTextField.setText(patron.getIdClasificacion());
            }
        }
    }

    @FXML
    private void guardarPatron() {
        String id = idTextField.getText();
        String name = nameTextField.getText();
        String contexto = contextoTextField.getText();
        String problema = problemaTextField.getText();
        String solucion = solucionTextField.getText();
        String ejemplos = ejemplosTextField.getText();
        String clasificacion = clasificacionTextField.getText();

        Patron patronModificado = new Patron(id, name, contexto, problema, solucion, ejemplos, clasificacion);
        try {
            patronXMLData.modificarPatron(id, patronModificado);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
