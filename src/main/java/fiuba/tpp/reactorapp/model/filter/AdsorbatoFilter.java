package fiuba.tpp.reactorapp.model.filter;

public class AdsorbatoFilter {

    private String nombreIUPAC;
    private Integer cargaIon;

    public AdsorbatoFilter(String nombreIUPAC, Integer cargaIon) {
        this.nombreIUPAC = nombreIUPAC;
        this.cargaIon = cargaIon;
    }

    public String getNombreIUPAC() {
        return nombreIUPAC;
    }

    public void setNombreIUPAC(String nombreIUPAC) {
        this.nombreIUPAC = nombreIUPAC;
    }

    public Integer getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Integer cargaIon) {
        this.cargaIon = cargaIon;
    }
}
