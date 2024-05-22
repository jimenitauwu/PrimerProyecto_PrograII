package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

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
