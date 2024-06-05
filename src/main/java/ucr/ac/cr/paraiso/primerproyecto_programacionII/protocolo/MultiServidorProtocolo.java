package ucr.ac.cr.paraiso.primerproyecto_programacionII.protocolo;

import org.jdom2.JDOMException;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.ClasificacionXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.data.PatronXMLData;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MultiServidorProtocolo {
    private PatronXMLData patronXMLData;
    private ClasificacionXMLData clasificacionXMLData;

    public MultiServidorProtocolo(PatronXMLData patronXMLData, ClasificacionXMLData clasificacionXMLData) {
        this.patronXMLData = patronXMLData;
        this.clasificacionXMLData = clasificacionXMLData;
    }

    public String procesarEntrada(String entrada, String comando) {
        if (entrada == null || entrada.isEmpty()) {
            return "<respuesta>Operación no especificada.</respuesta>";
        }

        try {
            switch (comando) {
                case "consultar":
                    return procesarConsulta(entrada);
                case "incluir":
                    return incluirPatron(entrada);
                case "modificar":
                    return modificarPatron(entrada);
                case "eliminar":
                    return eliminarPatron(entrada);
                case "incluir_clasificacion":
                    return incluirClasificacion(entrada);
                case "modificar_clasificacion":
                    return modificarClasificacion(entrada);
                case "eliminar_clasificacion":
                    return eliminarClasificacion(entrada);
                default:
                    return "<respuesta>Comando no reconocido.</respuesta>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al procesar la entrada.</respuesta>";
        }
    }

    private String procesarConsulta(String criterio) throws IOException, JDOMException {
        if (criterio.startsWith("patron ")) {
            String nombrePatron = criterio.substring(7);
            List<Patron> patrones = patronXMLData.obtenerPatrones();
            for (Patron patron : patrones) {
                if (patron.getName().equalsIgnoreCase(nombrePatron)) {
                    return patron.toXMLString();
                }
            }
            return "<respuesta>Patrón no encontrado.</respuesta>";
        } else if (criterio.startsWith("clasificacion ")) {
            String nombreClasificacion = criterio.substring(14);
            List<Clasificacion> clasificaciones = clasificacionXMLData.obtenerClasificaciones();
            for (Clasificacion clasificacion : clasificaciones) {
                if (clasificacion.getNameClasificacion().equalsIgnoreCase(nombreClasificacion)) {
                    return clasificacion.toXMLString();
                }
            }
            return "<respuesta>Clasificación no encontrada.</respuesta>";
        }
        return "<respuesta>Criterio de consulta no reconocido.</respuesta>";
    }

    private String incluirPatron(String datosPatron) {
        try {
            Patron nuevoPatron = Patron.fromXMLString(datosPatron);
            patronXMLData.insertarPatron(nuevoPatron);
            return "<respuesta>Patrón incluido exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al incluir el patrón.</respuesta>";
        }
    }

    private String modificarPatron(String datosPatron) {
        try {
            Patron patronModificado = Patron.fromXMLString(datosPatron);
            patronXMLData.modificarPatron(patronModificado);
            return "<respuesta>Patrón modificado exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al modificar el patrón.</respuesta>";
        }
    }

    private String eliminarPatron(String idPatron) {
        try {
            patronXMLData.eliminarPatron(idPatron);
            return "<respuesta>Patrón eliminado exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al eliminar el patrón.</respuesta>";
        }
    }

    private String incluirClasificacion(String datosClasificacion) {
        try {
            Clasificacion nuevaClasificacion = Clasificacion.fromXMLString(datosClasificacion);
            clasificacionXMLData.insertarClasificacion(nuevaClasificacion);
            return "<respuesta>Clasificación incluida exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al incluir la clasificación.</respuesta>";
        }
    }

    private String modificarClasificacion(String datosClasificacion) {
        try {
            Clasificacion clasificacionModificada = Clasificacion.fromXMLString(datosClasificacion);
            clasificacionXMLData.modificarClasificacion(clasificacionModificada);
            return "<respuesta>Clasificación modificada exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al modificar la clasificación.</respuesta>";
        }
    }

    private String eliminarClasificacion(String idClasificacion) {
        try {
            clasificacionXMLData.eliminarClasificacion(idClasificacion);
            return "<respuesta>Clasificación eliminada exitosamente.</respuesta>";
        } catch (Exception e) {
            e.printStackTrace();
            return "<respuesta>Error al eliminar la clasificación.</respuesta>";
        }
    }
}



