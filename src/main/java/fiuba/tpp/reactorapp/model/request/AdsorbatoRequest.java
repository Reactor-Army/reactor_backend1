package fiuba.tpp.reactorapp.model.request;

public class AdsorbatoRequest {

    private Long id;

    private String nombreIon;
    private String nombreIUPAC;
    private Integer cargaIon;
    private Float radioIonico;
    private Float limiteVertido;

    public AdsorbatoRequest() {
    }

    public AdsorbatoRequest(String nombreIon, String nombreIUPAC,Integer cargaIon, Float radioIonico, Float limiteVertido) {
        this.nombreIon = nombreIon;
        this.nombreIUPAC = nombreIUPAC;
        this.cargaIon = cargaIon;
        this.radioIonico = radioIonico;
        this.limiteVertido = limiteVertido;
    }

    public String getNombreIon() {
        return nombreIon;
    }

    public void setNombreIon(String nombreIon) {
        this.nombreIon = nombreIon;
    }

    public Integer getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Integer cargaIon) {
        this.cargaIon = cargaIon;
    }

    public Float getRadioIonico() {
        return radioIonico;
    }

    public void setRadioIonico(Float radioIonico) {
        this.radioIonico = radioIonico;
    }

    public Float getLimiteVertido() {
        return limiteVertido;
    }

    public void setLimiteVertido(Float limiteVertido) {
        this.limiteVertido = limiteVertido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreIUPAC() {
        return nombreIUPAC;
    }

    public void setNombreIUPAC(String nombreIUPAC) {
        this.nombreIUPAC = nombreIUPAC;
    }
}
