package ucr.ac.cr.paraiso.primerproyecto_programacionII;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.controller.HelloController;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Proyecto 1");
        stage.setScene(scene);

        // Obtén el controlador y establece la IP del servidor
        HelloController controller = fxmlLoader.getController();
        String serverIP = "192.168.1.2"; // Establece aquí la IP del servidor
        controller.setServerIP(serverIP);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
