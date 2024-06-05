package ucr.ac.cr.paraiso.primerproyecto_programacionII;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Patron;

import java.io.IOException;
import java.io.StringReader;

public class PatronModelo {

    private Patron patronBuscado;
    public PatronModelo() {

    }

    public Patron getPatronBuscado() {
        return patronBuscado;
    }

    public void setPatronBuscado(Patron patronBuscado) {
        this.patronBuscado = patronBuscado;
    }

    private String idPatron;
    private String name;
    private String problemaPatron;
    private String contextoPatron;
    private String solucionPatron;
    private String ejemplosPatron;
    private String idClasificacion;

    public PatronModelo(String idPatron, String name, String problemaPatron, String contextoPatron,
                        String solucionPatron, String ejemplosPatron, String idClasificacion) {
        this.idPatron = idPatron;
        this.name = name;
        this.problemaPatron = problemaPatron;
        this.contextoPatron = contextoPatron;
        this.solucionPatron = solucionPatron;
        this.ejemplosPatron = ejemplosPatron;
        this.idClasificacion = idClasificacion;
    }

    // Getters y setters para todos los atributos

    public String getIdPatron() {
        return idPatron;
    }

    public void setIdPatron(String idPatron) {
        this.idPatron = idPatron;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProblemaPatron() {
        return problemaPatron;
    }

    public void setProblemaPatron(String problemaPatron) {
        this.problemaPatron = problemaPatron;
    }

    public String getContextoPatron() {
        return contextoPatron;
    }

    public void setContextoPatron(String contextoPatron) {
        this.contextoPatron = contextoPatron;
    }

    public String getSolucionPatron() {
        return solucionPatron;
    }

    public void setSolucionPatron(String solucionPatron) {
        this.solucionPatron = solucionPatron;
    }

    public String getEjemplosPatron() {
        return ejemplosPatron;
    }

    public void setEjemplosPatron(String ejemplosPatron) {
        this.ejemplosPatron = ejemplosPatron;
    }

    public String getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(String idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public String toXMLString() {
        Element patronElement = new Element("patron");
        patronElement.setAttribute("idPatron", idPatron);
        patronElement.addContent(new Element("name").setText(name));
        patronElement.addContent(new Element("contextoPatron").setText(contextoPatron));
        patronElement.addContent(new Element("problemaPatron").setText(problemaPatron));
        patronElement.addContent(new Element("solucionPatron").setText(solucionPatron));
        patronElement.addContent(new Element("ejemploPatron").setText(ejemplosPatron));
        patronElement.addContent(new Element("idClasificacion").setText(idClasificacion));

        return new XMLOutputter(Format.getPrettyFormat()).outputString(patronElement);
    }

    public static Patron fromXMLString(String xmlString) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlString));

        Element rootElement = document.getRootElement();
        String idPatron = rootElement.getAttributeValue("idPatron");
        String name = rootElement.getChildText("name");
        String contextoPatron = rootElement.getChildText("contextoPatron");
        String problemaPatron = rootElement.getChildText("problemaPatron");
        String solucionPatron = rootElement.getChildText("solucionPatron");
        String ejemplosPatron = rootElement.getChildText("ejemploPatron");
        String idClasificacion = rootElement.getChildText("idClasificacion");

        return new Patron(idPatron, name, contextoPatron, problemaPatron, solucionPatron, ejemplosPatron, idClasificacion);
    }
}


