package ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo;

import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.util.ArrayList;
import java.util.List;

public class MultiServidorProtocolo {
    private List<Patron> patrones;

    public MultiServidorProtocolo() {
        patrones = new ArrayList<>();
        patrones.add(new Patron("1", "Contexto 1", "Problema 1", "Solución 1", List.of("Ejemplo 1A", "Ejemplo 1B"), "Clasificación 1"));
        patrones.add(new Patron("2", "Contexto 2", "Problema 2", "Solución 2", List.of("Ejemplo 2A", "Ejemplo 2B"), "Clasificación 2"));
        patrones.add(new Patron("3", "Contexto 3", "Problema 3", "Solución 3", List.of("Ejemplo 3A", "Ejemplo 3B"), "Clasificación 3"));
    }

    public String procesarEntrada(String entrada) {
        if (entrada == null || entrada.isEmpty()) {
            return "Escriba 'Patron 1', 'Patron 2' o 'Patron 3' para obtener información sobre algún patron.";
        }

        switch (entrada.toLowerCase()) {
            case  "patron 1":
                return patrones.get(0).toString();
            case "patron 2":
                return patrones.get(1).toString();
            case "patron 3":
                return patrones.get(2).toString();
            default:
                return "Entrada no reconocida. Escriba 'Patron 1', 'Patron 2' o 'Patron 3'.";
        }
    }
}
