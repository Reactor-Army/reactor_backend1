package fiuba.tpp.reactorapp.model.filter;

public class AdsorbatoFilter {

    private String nombre;
    private Integer cargaIon;

    public AdsorbatoFilter(String nombre, Integer cargaIon) {
        this.nombre = nombre;
        this.cargaIon = cargaIon;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Integer cargaIon) {
        this.cargaIon = cargaIon;
    }
}
