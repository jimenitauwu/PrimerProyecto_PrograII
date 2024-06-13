package ucr.ac.cr.paraiso.primerproyecto_programacionII.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.HelloApplication;
import java.io.IOException;

public class UtilityFx {
    public static void loadPage(String page, BorderPane bp){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
