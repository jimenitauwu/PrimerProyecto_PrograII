package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;


public class Patron {
    private String idPatron;
    private String name;
    private String contextoPatron;
    private String problemaPatron;
    private String solucionPatron;
    private String ejemplosPatron;
    private String idClasificacion;

    public Patron(String name, String contextoPatron, String problemaPatron, String solucionPatron, String ejemplosPatron, String idClasificacion) {
        this.name = name;
        this.contextoPatron = contextoPatron;
        this.problemaPatron = problemaPatron;
        this.solucionPatron = solucionPatron;
        this.ejemplosPatron = ejemplosPatron;
        this.idClasificacion = idClasificacion;
    }

    public Patron(String idPatron, String name, String contextoPatron, String problemaPatron, String solucionPatron, String ejemplosPatron, String idClasificacion) {
        this.idPatron = idPatron;
        this.name = name;
        this.contextoPatron = contextoPatron;
        this.problemaPatron = problemaPatron;
        this.solucionPatron = solucionPatron;
        this.ejemplosPatron = ejemplosPatron;
        this.idClasificacion = idClasificacion;
    }

    public Patron() {}
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
        Element patronElement = new Element("Patron");

        try {
            if (idPatron != null) {
                patronElement.setAttribute("id", idPatron);
            }
            if (name != null) {
                patronElement.addContent(new Element("Name").setText(name));
            }
            if (problemaPatron != null) {
                patronElement.addContent(new Element("Problema").setText(problemaPatron));
            }
            if (contextoPatron != null) {
                patronElement.addContent(new Element("Contexto").setText(contextoPatron));
            }
            if (solucionPatron != null) {
                patronElement.addContent(new Element("Solucion").setText(solucionPatron));
            }
            if (ejemplosPatron != null) {
                patronElement.addContent(new Element("Ejemplos").setText(ejemplosPatron));
            }
            if (idClasificacion != null) {
                patronElement.addContent(new Element("Clasificacion").setText(idClasificacion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convertir el Element a XML string
        return new XMLOutputter().outputString(new Document(patronElement));
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

        Patron patron = new Patron(idPatron, name, contextoPatron, problemaPatron, solucionPatron, ejemplosPatron, idClasificacion);
        System.out.println("Parsed Patron from XML: " + patron);
        return patron;
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
