module ucr.ac.cr.paraiso.xml.primerproyecto_programacionii {
    requires javafx.controls;
    requires javafx.fxml;


    opens ucr.ac.cr.paraiso.primerproyecto_programacionII to javafx.fxml;
    exports ucr.ac.cr.paraiso.primerproyecto_programacionII;
    exports ucr.ac.cr.paraiso.primerproyecto_programacionII.controller;
    opens ucr.ac.cr.paraiso.primerproyecto_programacionII.controller to javafx.fxml;
}