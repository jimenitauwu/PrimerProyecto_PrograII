package ucr.ac.cr.paraiso.primerproyecto_programacionII;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.controller.HelloController;

import java.io.IOException;
import java.net.InetAddress;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Proyecto 1");
        stage.setResizable(true);
        stage.setScene(scene);

        // Get the local machine's IP address dynamically
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Local IP Address: " + serverIP);

        // Set the server IP address for the controller
        HelloController controller = fxmlLoader.getController();
        controller.setServerIP(serverIP);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
