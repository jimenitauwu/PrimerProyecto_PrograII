package ucr.ac.cr.paraiso.primerproyecto_programacionII.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaDocumento));
        xmlOutputter.output(this.documento, System.out);
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

        // Obtener el nombre de la clasificación por id desde ClasificacionXMLData
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

    private String obtenerNombreClasificacionPorId(String idClasificacion) throws IOException, JDOMException {
        List<Clasificacion> clasificaciones = clasificacionData.obtenerClasificaciones();
        for (Clasificacion clasificacion : clasificaciones) {
            if (clasificacion.getIdClasificacion().equals(idClasificacion)) {
                return clasificacion.getNameClasificacion();
            }
        }
        return "";
    }

    public List<Patron> obtenerPatrones() throws IOException, JDOMException {
        List<Patron> patrones = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(rutaDocumento));
        Element rootElement = document.getRootElement();
        List<Element> patronElements = rootElement.getChildren("patron");

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

    public static void main(String[] args) throws IOException, JDOMException {
        ClasificacionXMLData clasificacionXMLData = new ClasificacionXMLData("clasificaciones.xml");
        PatronXMLData patronXMLData = new PatronXMLData("patrones.xml", clasificacionXMLData);

        // Insertar nuevas clasificaciones
        clasificacionXMLData.insertarClasificacion(new Clasificacion("1", "Comportamiento"));
        clasificacionXMLData.insertarClasificacion(new Clasificacion("2", "Creacional"));
        clasificacionXMLData.insertarClasificacion(new Clasificacion("3", "Estructural"));

        // Insertar patrones
        Patron patron1 = new Patron("1", "Estrategia", "Define una familia de algoritmos.", "Descripción del problema para Estrategia", "Descripción de la solución para Estrategia", "Descripción del ejemplo para Estrategia", "1");
        Patron patron2 = new Patron("2", "Singleton", "Garantiza que una clase solo tenga una instancia.", "Descripción del problema para Singleton", "Descripción de la solución para Singleton", "Descripción del ejemplo para Singleton", "2");
        Patron patron3 = new Patron("3", "Adaptador", "Convierte la interfaz de una clase en otra interfaz que los clientes esperan.", "Descripción del problema para Adaptador", "Descripción de la solución para Adaptador", "Descripción del ejemplo para Adaptador", "3");

        patronXMLData.insertarPatron(patron1);
        patronXMLData.insertarPatron(patron2);
        patronXMLData.insertarPatron(patron3);

        // Obtener patrones
        List<Patron> patrones = patronXMLData.obtenerPatrones();
        for (Patron p : patrones) {
            System.out.println("Patrón: " + p.getName() + ", Clasificación: " + p.getIdClasificacion());
        }
    }
}
