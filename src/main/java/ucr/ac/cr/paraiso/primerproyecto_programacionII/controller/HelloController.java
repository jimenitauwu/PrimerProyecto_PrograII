package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.HelloApplication;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;

import java.io.IOException;



public class HelloController {

    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private MenuBar menuBar;
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    @FXML
    private void initialize() {
        // Inicializar PatronXMLData y ClasificacionXMLData con la ruta correcta de tus archivos XML
        try {
            this.clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
            this.patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar el error apropiadamente, mostrar un mensaje o lo que necesites
        }
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            Parent root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if (controller instanceof BuscarPatronController || controller instanceof ModificarPatronController || controller instanceof BorrarPatronController || controller instanceof BorrarClasificacionController) {
                if (patronXMLData != null && clasificacionXMLData != null) {
                    switch (controller) {
                        case BuscarPatronController buscarPatronController -> {
                            buscarPatronController.setPatronXMLData(patronXMLData);
                            buscarPatronController.setClasificacionXMLData(clasificacionXMLData);
                        }
                        case ModificarPatronController modificarPatronController -> {
                            modificarPatronController.setPatronXMLData(patronXMLData);
                            modificarPatronController.setClasificacionXMLData(clasificacionXMLData);
                        }
                        case BorrarPatronController borrarPatronController -> {
                            borrarPatronController.setPatronXMLData(patronXMLData);
                            borrarPatronController.setClasificacionXMLData(clasificacionXMLData);
                        }
                        case BorrarClasificacionController borrarClasificacionController -> {
                            borrarClasificacionController.setClasificacionXMLData(clasificacionXMLData);
                        }
                        case BuscarClasificacionController buscarClasificacionController ->{
                            buscarClasificacionController.setClasificacionXMLData(clasificacionXMLData);}

                        default -> {
                        }
                    }
                } else {
                    System.err.println("patronXMLData o clasificacionXMLData es nulo.");
                    // Manejar el error de datos nulos apropiadamente
                }
            }
            this.bp.setCenter(root);
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
    public void anadirMenuBar(ActionEvent actionEvent) {
        loadPage("anadirClasificacion.fxml");
    }

    @FXML
    public void inicio(ActionEvent actionEvent) {
        bp.setCenter(ap);
        menuBar.setVisible(false);
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
    public void modificarPatron(ActionEvent actionEvent) {
        loadPage("modificarPatron.fxml");
    }

    @FXML
    public void eliminarPatron(ActionEvent actionEvent) {
        loadPage("BorrarPatron.fxml");
    }

    @FXML
    public void eliminarClasificacion(ActionEvent actionEvent) {
        loadPage("BorrarClasificacion.fxml");
    }

    @FXML
    public void modificarClasificacion(ActionEvent actionEvent) {
        // Implementación del método
    }
}
