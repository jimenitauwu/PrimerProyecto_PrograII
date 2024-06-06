package ucr.ac.cr.paraiso.primerproyecto_programacionII.data;

import java.io.File;
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
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

public class PatronXMLData {
    private String rutaDocumento;
    private Element raiz;
    private Document documento;
    private ClasificacionXMLData clasificacionData;

    public PatronXMLData(String rutaDocumento, ClasificacionXMLData clasificacionData) throws IOException, JDOMException {
        this.clasificacionData = clasificacionData;
        File file = new File(rutaDocumento);
        if (!file.exists()) {
            this.rutaDocumento = rutaDocumento;
            this.raiz = new Element("patrones");
            this.documento = new Document(raiz);
            guardar();
        } else {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(new File(rutaDocumento));
            this.raiz = documento.getRootElement();
            this.rutaDocumento = rutaDocumento;
        }
    }

    private void guardar() throws IOException {
        // Ordenar patrones antes de guardar
        ordenarPatrones();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaDocumento));
    }

    public void insertarPatron(Patron patron) throws IOException, JDOMException {
        Element ePatron = new Element("patron");
        ePatron.setAttribute("idPatron", patron.getIdPatron());

        Element eName = new Element("name");
        eName.addContent(patron.getName());

        Element eContexto = new Element("contextoPatron");
        eContexto.addContent(patron.getContextoPatron());

        Element eProblema = new Element("problemaPatron");
        eProblema.addContent(patron.getProblemaPatron());

        Element eSolucion = new Element("solucionPatron");
        eSolucion.addContent(patron.getSolucionPatron());

        Element eEjemplos = new Element("ejemploPatron");
        eEjemplos.addContent(patron.getEjemplosPatron());

        String nombreClasificacion = obtenerNombreClasificacionPorId(patron.getIdClasificacion());

        Element eClasificacion = new Element("clasificacion");
        eClasificacion.addContent(nombreClasificacion);

        ePatron.addContent(eName);
        ePatron.addContent(eContexto);
        ePatron.addContent(eProblema);
        ePatron.addContent(eSolucion);
        ePatron.addContent(eEjemplos);
        ePatron.addContent(eClasificacion);

        raiz.addContent(ePatron);
        guardar();
    }

    public void modificarPatron(Patron patron) throws IOException, JDOMException {
        List<Element> patronElements = raiz.getChildren("patron");
        for (Element ePatron : patronElements) {
            if (ePatron.getAttributeValue("idPatron").equals(patron.getIdPatron())) {
                ePatron.getChild("name").setText(patron.getName());
                ePatron.getChild("contextoPatron").setText(patron.getContextoPatron());
                ePatron.getChild("problemaPatron").setText(patron.getProblemaPatron());
                ePatron.getChild("solucionPatron").setText(patron.getSolucionPatron());
                ePatron.getChild("ejemploPatron").setText(patron.getEjemplosPatron());

                String nombreClasificacion = obtenerNombreClasificacionPorId(patron.getIdClasificacion());
                ePatron.getChild("clasificacion").setText(nombreClasificacion);

                guardar();
                return;
            }
        }
    }


    public void eliminarPatron(String idPatron) throws IOException {
        List<Element> patronElements = raiz.getChildren("patron");
        for (Element ePatron : patronElements) {
            if (ePatron.getAttributeValue("idPatron").equals(idPatron)) {
                raiz.removeContent(ePatron);
                guardar();
                return;
            }
        }
    }

    private String obtenerNombreClasificacionPorId(String idClasificacion) throws IOException, JDOMException {
        List<Clasificacion> clasificaciones = clasificacionData.obtenerClasificaciones();
        for (Clasificacion clasificacion : clasificaciones) {
            if (clasificacion.getIdClasificacion().equals(idClasificacion)) {
                return clasificacion.getNameClasificacion();
            }
        }
        return "";
    }

    public List<Patron> obtenerPatrones() {
        List<Patron> patrones = new ArrayList<>();
        List<Element> patronElements = raiz.getChildren("patron");

        for (Element ePatron : patronElements) {
            String idPatron = ePatron.getAttributeValue("idPatron");
            String name = ePatron.getChildText("name");
            String contextoPatron = ePatron.getChildText("contextoPatron");
            String problemaPatron = ePatron.getChildText("problemaPatron");
            String solucionPatron = ePatron.getChildText("solucionPatron");
            String ejemplosPatron = ePatron.getChildText("ejemploPatron");
            String idClasificacion = ePatron.getChildText("clasificacion");

            Patron patron = new Patron(idPatron, name, contextoPatron, problemaPatron, solucionPatron, ejemplosPatron, idClasificacion);
            patrones.add(patron);
        }

        return patrones;
    }

    public Patron obtenerPatronPorID(String idPatron) {
        List<Patron> patrones = obtenerPatrones();
        for (Patron patron : patrones) {
            if (patron.getIdPatron().equals(idPatron)) {
                return patron;
            }
        }
        return null; // Si no se encuentra ningún patrón con el ID especificado
    }



    private void ordenarPatrones() {
        List<Element> patronElements = new ArrayList<>(raiz.getChildren("patron"));
        patronElements.sort(Comparator.comparing(e -> e.getChildText("name")));
        raiz.removeChildren("patron");
        raiz.addContent(patronElements);
    }

    public String generarNuevoIdPatron() {
        List<Patron> patrones = obtenerPatrones();
        int maxId = 0;
        for (Patron patron : patrones) {
            int id = Integer.parseInt(patron.getIdPatron());
            if (id > maxId) {
                maxId = id;
            }
        }
        return String.valueOf(maxId + 1);
    }

}