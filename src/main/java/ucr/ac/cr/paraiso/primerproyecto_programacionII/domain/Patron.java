package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class Patron {
    private String idPatron;
    private String name;
    private String contextoPatron;
    private String problemaPatron;
    private String solucionPatron;
    private String ejemplosPatron;
    private String idClasificacion;

    public Patron(String idPatron, String name, String contextoPatron, String problemaPatron, String solucionPatron, String ejemplosPatron, String idClasificacion) {
        this.idPatron = idPatron;
        this.name = name;
        this.contextoPatron = contextoPatron;
        this.problemaPatron = problemaPatron;
        this.solucionPatron = solucionPatron;
        this.ejemplosPatron = ejemplosPatron;
        this.idClasificacion = idClasificacion;
    }

    public Patron() {
    }

    public String getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(String idClasificacion) {
        this.idClasificacion = idClasificacion;
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

    public String getIdPatron() {
        return idPatron;
    }

    public void setIdPatron(String idPatron) {
        this.idPatron = idPatron;
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

    public String toXMLString() {
        return "<patron idPatron=\"" + idPatron + "\">\n" +
                "    <name>" + name + "</name>\n" +
                "    <contextoPatron>" + contextoPatron + "</contextoPatron>\n" +
                "    <problemaPatron>" + problemaPatron + "</problemaPatron>\n" +
                "    <solucionPatron>" + solucionPatron + "</solucionPatron>\n" +
                "    <ejemploPatron>" + ejemplosPatron + "</ejemploPatron>\n" +
                "    <clasificacion>" + idClasificacion + "</clasificacion>\n" +
                "</patron>";
    }

    public static Patron fromXMLString(String xmlString) throws JDOMException, IOException {
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


    @Override
    public String toString() {
        return "Patron{" +
                "idPatron='" + idPatron + '\'' +
                ", name='" + name + '\'' +
                ", contextoPatron='" + contextoPatron + '\'' +
                ", problemaPatron='" + problemaPatron + '\'' +
                ", solucionPatron='" + solucionPatron + '\'' +
                ", ejemplosPatron='" + ejemplosPatron + '\'' +
                ", clasificacion='" + idClasificacion + '\'' +
                '}';
    }
}
