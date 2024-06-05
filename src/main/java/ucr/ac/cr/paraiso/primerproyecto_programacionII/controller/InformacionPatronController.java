package ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.PatronModelo;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

public class InformacionPatronController {
    @javafx.fxml.FXML
    private TextArea txtID;
    @javafx.fxml.FXML
    private TextArea txtNombre;
    @javafx.fxml.FXML
    private TextArea txtProblema;
    @javafx.fxml.FXML
    private TextArea txtClasificacion;
    @javafx.fxml.FXML
    private ImageView imgView;
    @javafx.fxml.FXML
    private TextArea txtSolucion;
    @javafx.fxml.FXML
    private TextArea txtContexto;
    @javafx.fxml.FXML
    private Button btnMenu;
    @javafx.fxml.FXML
    private TextArea txtEjemplos;

    private PatronModelo patronModelo;

    public void setPatronModelo(PatronModelo patronModelo) {
        this.patronModelo = patronModelo;
    }

    public void setPatron(Patron patron) {
        // Si el patrón se almacena en un objeto PatronModelo, puedes hacer la conversión aquí
        PatronModelo modelo = new PatronModelo(patron.getIdPatron(), patron.getName(), patron.getProblemaPatron(),
                patron.getContextoPatron(), patron.getSolucionPatron(),
                patron.getEjemplosPatron(), patron.getIdClasificacion());
        setPatronModelo(modelo);
    }

    @javafx.fxml.FXML
    public void MenuOnAction(ActionEvent actionEvent) {
    }
}
