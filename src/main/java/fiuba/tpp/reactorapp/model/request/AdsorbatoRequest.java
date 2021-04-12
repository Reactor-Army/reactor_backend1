package fiuba.tpp.reactorapp.model.request;

public class AdsorbatoRequest {

    private String nombreIon;
    private Float cargaIon;
    private Float radioIonico;
    private Float limiteVertido;

    public AdsorbatoRequest() {
    }

    public AdsorbatoRequest(String nombreIon, Float cargaIon, Float radioIonico, Float limiteVertido) {
        this.nombreIon = nombreIon;
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

    public Float getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Float cargaIon) {
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
}
