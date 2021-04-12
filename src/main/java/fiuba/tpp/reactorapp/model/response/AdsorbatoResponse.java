package fiuba.tpp.reactorapp.model.response;

import fiuba.tpp.reactorapp.entities.Adsorbato;

public class AdsorbatoResponse {

    private Long id;
    private String nombreIon;
    private Float cargaIon;
    private Float radioIonico;
    private Float limiteVertido;


    public AdsorbatoResponse() {
    }

    public AdsorbatoResponse(Adsorbato adsorbato) {
        this.id = adsorbato.getId();
        this.cargaIon = adsorbato.getCargaIon();
        this.nombreIon = adsorbato.getNombreIon();
        this.radioIonico = adsorbato.getRadioIonico();
        this.limiteVertido = adsorbato.getLimiteVertido();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
