package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.HelloApplication;

import java.io.IOException;

public class HelloController {

    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private MenuBar menuBar;

    private void loadPage(String page){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void creacionales(ActionEvent actionEvent) {
        loadPage("creacionales.fxml");
        menuBar.setVisible(true);
    }


    @FXML
    public void comportamiento(ActionEvent actionEvent) {
        loadPage("comportamiento.fxml");
        menuBar.setVisible(true);
    }

    @FXML
    public void estructurales(ActionEvent actionEvent) {
        loadPage("estructurales.fxml");
        menuBar.setVisible(true);
    }

    @FXML
    public void salir(ActionEvent actionEvent) {
        System.exit(0);

    }

    @FXML
    public void modificarMenuBar(ActionEvent actionEvent) {
        loadPage("modificar.fxml");
    }

    @FXML
    public void anadirMenuBar(ActionEvent actionEvent) {
        loadPage("anadir.fxml");
    }


    @FXML
    public void inicio(ActionEvent actionEvent) {
        bp.setCenter(ap);
        menuBar.setVisible(false);
    }

    @FXML
    public void modificarPatronMenuBar(ActionEvent actionEvent) {
        loadPage("modificarPatron.fxml");
    }

    @FXML
    public void anadirPatronMenuBar(ActionEvent actionEvent) {
        loadPage("anadirPatron.fxml");
    }

    @FXML
    public void buscarClasificacion(ActionEvent actionEvent) {
        loadPage("BuscarClasificacion.fxml");
    }

    @FXML
    public void buscarPatron(ActionEvent actionEvent) {
        loadPage("BuscarPatron.fxml");
    }

    @FXML
    public void eliminarPatron(ActionEvent actionEvent) {
        loadPage("BorrarClasificacion.fxml");
    }

    @FXML
    public void eliminarClasificacion(ActionEvent actionEvent) {
        loadPage("BorrarPatron.fxml");
    }
}