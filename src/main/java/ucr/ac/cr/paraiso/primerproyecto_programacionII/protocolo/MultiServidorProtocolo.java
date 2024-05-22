package ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo;

import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class MultiServidorProtocolo {
    private PatronXMLData patronXMLData;

    public MultiServidorProtocolo(PatronXMLData patronXMLData) {
        this.patronXMLData = patronXMLData;
    }

    public String procesarEntrada(String entrada) {
        if (entrada == null || entrada.isEmpty()) {
            return "Escriba 'Patron 1', 'Patron 2' o 'Patron 3' para obtener información sobre algún patrón.";
        }

        try {
            switch (entrada.toLowerCase()) {
                case "patron 1":
                case "patron 2":
                case "patron 3":
                    List<Patron> patrones = patronXMLData.obtenerPatrones();
                    int index = Integer.parseInt(entrada.split(" ")[1]) - 1;
                    if (index >= 0 && index < patrones.size()) {
                        return patrones.get(index).toString();
                    } else {
                        return "Patrón no encontrado.";
                    }
                default:
                    return "Entrada no reconocida. Escriba 'Patron 1', 'Patron 2' o 'Patron 3'.";
            }
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            return "Error al procesar la entrada.";
        }
    }
}

