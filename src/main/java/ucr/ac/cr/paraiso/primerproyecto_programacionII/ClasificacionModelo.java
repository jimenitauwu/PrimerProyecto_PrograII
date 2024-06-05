package ucr.ac.cr.paraiso.primerproyecto_programacionII;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;

public class ClasificacionModelo {
    private String idClasificacion;
    private String nombre;

    public ClasificacionModelo() {
    }

    public ClasificacionModelo(String idClasificacion, String nombre) {
        this.idClasificacion = idClasificacion;
        this.nombre = nombre;
    }

    public String getIdClasificacion() {
        return idClasificacion;
    }

    public void setIdClasificacion(String idClasificacion) {
        this.idClasificacion = idClasificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toXMLString() {
        Element clasificacionElement = new Element("clasificacion");
        clasificacionElement.setAttribute("idClasificacion", idClasificacion);
        clasificacionElement.addContent(new Element("nombre").setText(nombre));
        return new XMLOutputter(Format.getPrettyFormat()).outputString(clasificacionElement);
    }

    public static ClasificacionModelo fromXMLString(String xmlString) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlString));

        Element rootElement = document.getRootElement();
        String idClasificacion = rootElement.getAttributeValue("idClasificacion");
        String nombre = rootElement.getChildText("nombre");

        return new ClasificacionModelo(idClasificacion, nombre);
    }
}

