package ucr.ac.cr.paraiso.primerproyecto_programacionII.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

public class ClasificacionXMLData {
    private String xmlFilePath;
    private Document document;
    private String rutaDocumento;
    private Element raiz;
    private Document documento;

    public ClasificacionXMLData(String rutaDocumento) throws IOException, JDOMException {
        File file = new File(rutaDocumento);
        if (!file.exists()) {
            this.rutaDocumento = rutaDocumento;
            this.raiz = new Element("clasificaciones");
            this.documento = new Document(raiz);
            save();
        } else {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(new File(rutaDocumento));
            this.raiz = documento.getRootElement();
            this.rutaDocumento = rutaDocumento;
        }
    }



    public void insertarClasificacion(Clasificacion clasificacion) throws IOException {
        Element eClasificacion = new Element("clasificacion");

        Element idClasificacion = new Element("idClasificacion");
        idClasificacion.addContent(clasificacion.getIdClasificacion());

        Element eNameClasificacion = new Element("nameClasificacion");
        eNameClasificacion.addContent(clasificacion.getNameClasificacion());

        eClasificacion.addContent(idClasificacion);
        eClasificacion.addContent(eNameClasificacion);

        raiz.addContent(eClasificacion);
        save();
    }

    public void modificarClasificacion(String idClasificacion, Clasificacion clasificacionModificada) throws IOException {
        Element save = null;
        List<Element> clasificacionElements = raiz.getChildren("clasificacion");

        // Buscar la clasificación a modificar por su ID
        for (Element eClasificacion : clasificacionElements) {
            if (eClasificacion.getChildText("idClasificacion").equals(idClasificacion)) {
                save = eClasificacion;
                break;
            }
        }

        if (save != null) {
            // Modificar el nombre de la clasificación
            save.getChild("nameClasificacion").setText(clasificacionModificada.getNameClasificacion());
            save();
        }
    }



    public void eliminarClasificacion(String idClasificacion) throws IOException {
        List<Element> clasificacionElements = raiz.getChildren("clasificacion");
        for (Element eClasificacion : clasificacionElements) {
            if (eClasificacion.getChildText("idClasificacion").equals(idClasificacion)) {
                raiz.removeContent(eClasificacion);
                save();
                return;
            }
        }
    }

    public List<Clasificacion> obtenerClasificaciones() {
        List<Clasificacion> clasificaciones = new ArrayList<>();
        List<Element> clasificacionElements = raiz.getChildren("clasificacion");

        for (Element eClasificacion : clasificacionElements) {
            String idClasificacion = eClasificacion.getChildText("idClasificacion");
            String nameClasificacion = eClasificacion.getChildText("nameClasificacion");

            Clasificacion clasificacion = new Clasificacion(idClasificacion, nameClasificacion);
            clasificaciones.add(clasificacion);
        }

        return clasificaciones;
    }

    private void ordenarClasificaciones() {
        List<Element> clasificacionElements = new ArrayList<>(raiz.getChildren("clasificacion"));
        clasificacionElements.sort(Comparator.comparing(e -> e.getChildText("nameClasificacion")));
        raiz.removeChildren("clasificacion");
        raiz.addContent(clasificacionElements);
    }

    public Clasificacion obtenerClasificacionPorId(String idClasificacion) throws IOException, JDOMException {
        List<Clasificacion> clasificaciones = obtenerClasificaciones();
        for (Clasificacion clasificacion : clasificaciones) {
            if (clasificacion.getIdClasificacion().equals(idClasificacion)) {
                return clasificacion;
            }
        }
        return null; // Si no se encuentra ninguna clasificación con ese ID
    }

    private void save() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileWriter(xmlFilePath));
    }

    public String generarNuevoIdClasificacion() {
        List<Clasificacion> clasificaciones = obtenerClasificaciones();
        int maxId = 0;
        for (Clasificacion clasificacion : clasificaciones) {
            int id = Integer.parseInt(clasificacion.getIdClasificacion());
            if (id > maxId) {
                maxId = id;
            }
        }
        return String.valueOf(maxId + 1);
    }

}

