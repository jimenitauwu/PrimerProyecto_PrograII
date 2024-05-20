package ucr.ac.cr.paraiso.primerproyecto_programacionII.domain;

public class Clasificacion {
    private String name;
    private String type;

    public Clasificacion() {
    }

    public Clasificacion(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Clasificacion{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
