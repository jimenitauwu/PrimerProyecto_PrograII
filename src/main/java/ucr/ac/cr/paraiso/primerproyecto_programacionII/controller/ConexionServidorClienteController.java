package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo.MultiServidorProtocolo;

public class ConexionServidorClienteController
{
    @javafx.fxml.FXML
    private TextArea textArea_Servidor;
    @javafx.fxml.FXML
    private TextField textField_Cliente;
    @javafx.fxml.FXML
    private Button btn_enviar;

    MultiServidorProtocolo protocolo= new MultiServidorProtocolo();



    @javafx.fxml.FXML
    public void initialize() {

        textArea_Servidor.setText("Escriba 'Patron 1', 'Patron 2' o 'Patron 3' para obtener información sobre algún patron.");
        textArea_Servidor.appendText("Cliente: " + textField_Cliente.getText() + "\n");

    }

    @javafx.fxml.FXML
    public void EnviarOnAction(ActionEvent actionEvent) {

        textArea_Servidor.appendText("Cliente: " + textField_Cliente.getText() + "\n");
        textArea_Servidor.appendText("Servidor: " + protocolo.procesarEntrada(textField_Cliente.getText()) + "\n");

        textField_Cliente.clear();

    }
}