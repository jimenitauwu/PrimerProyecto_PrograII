package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

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


    public String toXMLString() {
        return "<clasificacion>\n" +
                "    <idClasificacion>" + idClasificacion + "</idClasificacion>\n" +
                "    <nameClasificacion>" + nameClasificacion + "</nameClasificacion>\n" +
                "</clasificacion>";
    }

    @Override
    public String toString() {
        return "Clasificacion{" +
                "name='" + nameClasificacion + '\'' +
                ", type='" + idClasificacion + '\'' +
                '}';
    }
}
