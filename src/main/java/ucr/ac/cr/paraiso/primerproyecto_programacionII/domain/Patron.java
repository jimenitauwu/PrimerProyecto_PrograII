package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

import java.util.List;

public class Patron {
    private String idPatron;
    private String contextoPatron;
    private String problemaPatron;
    private String solucionPatron;
    private List<String> ejemplosPatron;
    private String clasificacion;

    public Patron() {
    }

    public Patron(String idPatron, String contextoPatron, String problemaPatron, String solucionPatron,
                  List<String> ejemplosPatron, String clasificacion) {
        this.idPatron = idPatron;
        this.contextoPatron = contextoPatron;
        this.problemaPatron = problemaPatron;
        this.solucionPatron = solucionPatron;
        this.ejemplosPatron = ejemplosPatron;
        this.clasificacion = clasificacion;
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

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public List<String> getEjemplosPatron() {
        return ejemplosPatron;
    }

    public void setEjemplosPatron(List<String> ejemplosPatron) {
        this.ejemplosPatron = ejemplosPatron;
    }


    @Override
    public String toString() {
        return "Patron{" +
                "idPatron='" + idPatron + '\'' +
                ", contextoPatron='" + contextoPatron + '\'' +
                ", problemaPatron='" + problemaPatron + '\'' +
                ", solucionPatron='" + solucionPatron + '\'' +
                ", ejemplosPatron=" + ejemplosPatron +
                ", clasificacion='" + clasificacion + '\'' +
                '}';
    }
}
