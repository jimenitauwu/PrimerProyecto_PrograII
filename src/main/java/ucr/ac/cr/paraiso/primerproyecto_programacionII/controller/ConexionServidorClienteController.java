package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo.MultiServidorProtocolo;
import java.io.IOException;

public class ConexionServidorClienteController
{
    @javafx.fxml.FXML
    private TextArea textArea_Servidor;
    @javafx.fxml.FXML
    private TextField textField_Cliente;
    @javafx.fxml.FXML
    private Button btn_enviar;

    private MultiServidorProtocolo protocolo;
    @javafx.fxml.FXML
    private Button btn_Salir;


    @javafx.fxml.FXML
    public void initialize() {
        textArea_Servidor.setText("Escriba 'Patron 1', 'Patron 2' o 'Patron 3' para obtener información sobre algún patrón.");
        textArea_Servidor.appendText("Cliente: " + textField_Cliente.getText() + "\n");

        // Create an instance of ClasificacionXMLData
        ClasificacionXMLData clasificacionXMLData;
        try {
            clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return;
        }

        // Create an instance of PatronXMLData with ClasificacionXMLData
        PatronXMLData patronXMLData;
        try {
            patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return;
        }

        // Create an instance of MultiServidorProtocolo with PatronXMLData
        protocolo = new MultiServidorProtocolo(patronXMLData, clasificacionXMLData);
    }

    @javafx.fxml.FXML
    public void EnviarOnAction(ActionEvent actionEvent) {
        textArea_Servidor.appendText("Cliente: " + textField_Cliente.getText() + "\n");
        textArea_Servidor.appendText("Servidor: " + protocolo.procesarEntrada(textField_Cliente.getText()) + "\n");
        textField_Cliente.clear();
    }

    @javafx.fxml.FXML
    public void SalirOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}