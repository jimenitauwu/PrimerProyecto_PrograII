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

    // Añade una variable para la IP del servidor
    private String serverIP;

    // Añade un método para establecer la IP del servidor
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @FXML
    private void initialize() {
        loadXMLData();
    }

    // Método separado para cargar los datos XML
    private void loadXMLData() {
        try {
            this.clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
            this.patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método separado para configurar los datos XML en el controlador
    private void configureXMLData(Object controller) {
        if (controller instanceof BuscarPatronController buscarPatronController) {
            buscarPatronController.setPatronXMLData(patronXMLData);
            buscarPatronController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof ModificarPatronController modificarPatronController) {
            modificarPatronController.setPatronXMLData(patronXMLData);
            modificarPatronController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof BorrarPatronController borrarPatronController) {
            borrarPatronController.setPatronXMLData(patronXMLData);
            borrarPatronController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof BorrarClasificacionController borrarClasificacionController) {
            borrarClasificacionController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof BuscarClasificacionController buscarClasificacionController) {
            buscarClasificacionController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof ModificarClasificacionController modificarClasificacionController) {
            modificarClasificacionController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof EstructuralesController estructuralesController) {
            estructuralesController.setPatronXMLData(patronXMLData);
            estructuralesController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof ComportamientoController comportamientoController) {
            comportamientoController.setPatronXMLData(patronXMLData);
            comportamientoController.setClasificacionXMLData(clasificacionXMLData);
        } else if (controller instanceof CreacionalesController creacionalesController) {
            creacionalesController.setPatronXMLData(patronXMLData);
            creacionalesController.setClasificacionXMLData(clasificacionXMLData);
        }
    }

    // Método separado para configurar la IP del servidor en el controlador
    private void configureServerIP(Object controller) {
        if (controller instanceof BuscarPatronController buscarPatronController) {
            buscarPatronController.setServerIP(serverIP);
        } else if (controller instanceof ModificarPatronController modificarPatronController) {
            modificarPatronController.setServerIP(serverIP);
        } else if (controller instanceof BorrarPatronController borrarPatronController) {
            borrarPatronController.setServerIP(serverIP);
        } else if (controller instanceof BorrarClasificacionController borrarClasificacionController) {
            borrarClasificacionController.setServerIP(serverIP);
        } else if (controller instanceof BuscarClasificacionController buscarClasificacionController) {
            buscarClasificacionController.setServerIP(serverIP);
        } else if (controller instanceof ModificarClasificacionController modificarClasificacionController) {
            modificarClasificacionController.setServerIP(serverIP);
        } else if (controller instanceof AnadirPatronController anadirPatronController) {
            anadirPatronController.setServerIP(serverIP);
        } else if (controller instanceof AnadirClasificacionController anadirClasificacionController) {
            anadirClasificacionController.setServerIP(serverIP);
        } else if (controller instanceof EstructuralesController estructuralesController) {
            estructuralesController.setServerIP(serverIP);
        } else if (controller instanceof ComportamientoController comportamientoController) {
            comportamientoController.setServerIP(serverIP);
        } else if (controller instanceof CreacionalesController creacionalesController) {
            creacionalesController.setServerIP(serverIP);
        }
    }

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            Parent root = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            configureXMLData(controller);
            configureServerIP(controller);
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
        menuBar.setVisible(true);
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
        loadPage("modificarClasificacion.fxml");
    }
}




