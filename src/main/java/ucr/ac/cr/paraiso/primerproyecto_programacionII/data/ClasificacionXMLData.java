package ucr.ac.cr.paraiso.primerproyecto_programacionII.data;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ucr.ac.cr.paraiso.primerproyecto_programacionII.domain.Clasificacion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClasificacionXMLData {

    private String rutaDocumento;
    private Element raiz;
    private Document documento;

    public ClasificacionXMLData(String rutaDocumento) throws IOException, JDOMException {
        File file = new File(rutaDocumento);
        if (!file.exists()) {
            this.rutaDocumento = rutaDocumento;
            this.raiz = new Element("clasificaciones");
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

    public void insertarClasificacion(Clasificacion clasificacion) throws IOException {
        Element eClasificacion = new Element("clasificacion");

        Element eName = new Element("name");
        eName.addContent(clasificacion.getName());

        Element eType = new Element("type");
        eType.addContent(clasificacion.getType());

        eClasificacion.addContent(eName);
        eClasificacion.addContent(eType);

        raiz.addContent(eClasificacion);
        guardar();
    }

    public List<Clasificacion> obtenerClasificaciones() throws IOException, JDOMException {
        List<Clasificacion> clasificaciones = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(rutaDocumento));
        Element rootElement = document.getRootElement();
        List<Element> clasificacionElements = rootElement.getChildren("clasificacion");

        for (Element eClasificacion : clasificacionElements) {
            String name = eClasificacion.getChildText("name");
            String type = eClasificacion.getChildText("type");

            Clasificacion clasificacion = new Clasificacion(name, type);
            clasificaciones.add(clasificacion);
        }

        return clasificaciones;
    }

}
