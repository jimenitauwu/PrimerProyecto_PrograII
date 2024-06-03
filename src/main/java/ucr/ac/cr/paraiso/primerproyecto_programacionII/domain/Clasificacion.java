package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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

    // Método para convertir una representación XML en una instancia de Clasificacion
    public static Clasificacion fromXMLString(String xmlString) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlString));

        Element root = document.getRootElement();
        String idClasificacion = root.getChildText("idClasificacion");
        String nameClasificacion = root.getChildText("nameClasificacion");

        return new Clasificacion(idClasificacion, nameClasificacion);
    }

    // Método para convertir la instancia de Clasificacion en una representación XML
    public String toXMLString() {
        Element root = new Element("clasificacion");
        Element idClasificacionElement = new Element("idClasificacion");
        idClasificacionElement.setText(this.idClasificacion);
        Element nameClasificacionElement = new Element("nameClasificacion");
        nameClasificacionElement.setText(this.nameClasificacion);
        root.addContent(idClasificacionElement);
        root.addContent(nameClasificacionElement);

        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        StringWriter stringWriter = new StringWriter();
        try {
            xmlOutput.output(new Document(root), stringWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    @Override
    public String toString() {
        return "Clasificacion{" +
                "name='" + nameClasificacion + '\'' +
                ", type='" + idClasificacion + '\'' +
                '}';
    }
}
