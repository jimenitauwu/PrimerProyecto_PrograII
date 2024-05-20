package ucr.ac.cr.paraiso.primerproyecto_programacionII.data;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatronXMLData {

    private String rutaDocumento;
    private Element raiz;
    private Document documento;

    public PatronXMLData(String rutaDocumento) throws IOException, JDOMException {
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
        xmlOutputter.output(this.documento, System.out);//Imprime el xml en consola
    }

    public void insertarPatron(Patron patron) throws IOException {
        Element ePatron = new Element("patron");
        ePatron.setAttribute("idPatron", patron.getIdPatron());

        Element eContexto = new Element("contextoPatron");
        eContexto.addContent(patron.getContextoPatron());

        Element eProblema = new Element("problemaPatron");
        eProblema.addContent(patron.getProblemaPatron());

        Element eSolucion = new Element("solucionPatron");
        eSolucion.addContent(patron.getSolucionPatron());

        Element eEjemplos = new Element("ejemplosPatron");
        for (String ejemplo : patron.getEjemplosPatron()) {
            Element eEjemplo = new Element("ejemplo");
            eEjemplo.addContent(ejemplo);
            eEjemplos.addContent(eEjemplo);
        }

        Element eClasificacion = new Element("clasificacion");
        eClasificacion.addContent(patron.getClasificacion());

        ePatron.addContent(eContexto);
        ePatron.addContent(eProblema);
        ePatron.addContent(eSolucion);
        ePatron.addContent(eEjemplos);
        ePatron.addContent(eClasificacion);

        raiz.addContent(ePatron);
        guardar();
    }

    public List<Patron> obtenerPatrones() throws IOException, JDOMException {
        List<Patron> patrones = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(rutaDocumento));
        Element rootElement = document.getRootElement();
        List<Element> patronElements = rootElement.getChildren("patron");

        for (Element ePatron : patronElements) {
            String idPatron = ePatron.getAttributeValue("idPatron");
            String contextoPatron = ePatron.getChildText("contextoPatron");
            String problemaPatron = ePatron.getChildText("problemaPatron");
            String solucionPatron = ePatron.getChildText("solucionPatron");

            List<String> ejemplosPatron = new ArrayList<>();
            for (Element eEjemplo : ePatron.getChild("ejemplosPatron").getChildren("ejemplo")) {
                ejemplosPatron.add(eEjemplo.getText());
            }

            String clasificacion = ePatron.getChildText("clasificacion");

            Patron patron = new Patron(idPatron, contextoPatron, problemaPatron, solucionPatron, ejemplosPatron, clasificacion);
            patrones.add(patron);
        }

        return patrones;
    }

}
