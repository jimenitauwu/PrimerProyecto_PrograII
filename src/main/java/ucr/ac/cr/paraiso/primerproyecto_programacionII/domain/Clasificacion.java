package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
public class Clasificacion {
    private String idClasificacion;
    private String nameClasificacion;

    public Clasificacion(String idClasificacion, String nameClasificacion) {
        this.idClasificacion = idClasificacion;
        this.nameClasificacion = nameClasificacion;
    }

    public Clasificacion() {
    }

    public String getNameClasificacion() {
        return nameClasificacion;
    }

    public void setNameClasificacion(String nameClasificacion) {
        this.nameClasificacion = nameClasificacion;
    }

    public String getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(String idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    // Método para convertir la instancia de Clasificacion en una representación XML
    public String toXMLString() {
        Element clasificacionElement = new Element("Clasificacion");

        try {
            if (idClasificacion != null) {
                clasificacionElement.setAttribute("IDclasificacion", idClasificacion);
            }
            if (nameClasificacion != null) {
                clasificacionElement.addContent(new Element("Name").setText(nameClasificacion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convertir el Element a XML string
        return new XMLOutputter(Format.getPrettyFormat()).outputString(new Document(clasificacionElement));
    }

    // Método para convertir una representación XML en una instancia de Clasificacion
    public static Clasificacion fromXMLString(String xmlString) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlString));

        Element rootElement = document.getRootElement();
        String idClasificacion = rootElement.getAttributeValue("IDclasificacion");
        String nameClasificacion = rootElement.getChildText("Name");

        Clasificacion clasificacion = new Clasificacion(idClasificacion, nameClasificacion);
        System.out.println("Parsed Clasificacion from XML: " + clasificacion);
        return clasificacion;
    }

    @Override
    public String toString() {
        return "Clasificacion{" +
                "idClasificacion='" + idClasificacion + '\'' +
                ", nameClasificacion='" + nameClasificacion + '\'' +
                '}';
    }
}