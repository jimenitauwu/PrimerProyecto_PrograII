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
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.util.Objects;


public class PatronXMLData {
    private String xmlFilePath;
    private Document document;
    private Element raiz;
    private ClasificacionXMLData clasificacionData;

    public PatronXMLData(String xmlFilePath, ClasificacionXMLData clasificacionData) throws JDOMException, IOException {
        this.xmlFilePath = Objects.requireNonNull(xmlFilePath);
        this.clasificacionData = Objects.requireNonNull(clasificacionData);

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(xmlFilePath);
        if (!xmlFile.exists()) {
            this.raiz = new Element("patrones");
            this.document = new Document(raiz);
            save();
        } else {
            document = builder.build(xmlFile);
            raiz = document.getRootElement();
        }
    }

    public void insertarPatron(Patron patron) throws IOException {
        String nuevoId = generarNuevoIdPatron();
        patron.setIdPatron(nuevoId);

        Element ePatron = new Element("patron");
        ePatron.setAttribute("idPatron", nuevoId);

        Element eName = new Element("name");
        eName.addContent(patron.getName());

        Element eContexto = new Element("contextoPatron");
        eContexto.addContent(patron.getContextoPatron());

        Element eProblema = new Element("problemaPatron");
        eProblema.addContent(patron.getProblemaPatron());

        Element eSolucion = new Element("solucionPatron");
        eSolucion.addContent(patron.getSolucionPatron());

        Element eEjemplos = new Element("ejemplosPatron");
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
        save();
    }

    public void modificarPatron(String idPatron, Patron patronModificado) throws IOException {
        Element save = null;
        List<Element> patronElements = raiz.getChildren("patron");
        for (Element ePatron : patronElements) {
            if (ePatron.getAttributeValue("idPatron").equals(idPatron)) {
                save = ePatron;
                break;
            }
        }

        if (save != null) {
            if (patronModificado.getName() != null) {
                save.getChild("name").setText(patronModificado.getName());
            }
            if (patronModificado.getContextoPatron() != null) {
                save.getChild("contextoPatron").setText(patronModificado.getContextoPatron());
            }
            if (patronModificado.getProblemaPatron() != null) {
                save.getChild("problemaPatron").setText(patronModificado.getProblemaPatron());
            }
            if (patronModificado.getSolucionPatron() != null) {
                save.getChild("solucionPatron").setText(patronModificado.getSolucionPatron());
            }
            if (patronModificado.getEjemplosPatron() != null) {
                save.getChild("ejemplosPatron").setText(patronModificado.getEjemplosPatron());
            }
            if (patronModificado.getIdClasificacion() != null) {
                String nombreClasificacion = obtenerNombreClasificacionPorId(patronModificado.getIdClasificacion());
                save.getChild("clasificacion").setText(nombreClasificacion);
            }
            save();
        }
    }

    public void eliminarPatron(String idPatron) throws IOException {
        List<Element> patronElements = raiz.getChildren("patron");
        for (Element ePatron : patronElements) {
            if (ePatron.getAttributeValue("idPatron").equals(idPatron)) {
                raiz.removeContent(ePatron);
                save();
                return;
            }
        }
    }


    private String obtenerNombreClasificacionPorId(String idClasificacion) {
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
            String ejemplosPatron = ePatron.getChildText("ejemplosPatron");
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
        return null;
    }

    private void save() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileWriter(xmlFilePath));
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