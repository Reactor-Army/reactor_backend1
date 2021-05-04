package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Adsorbate;

public class AdsorbateResponse {

    private Long id;

    @JsonProperty("nombreIon")
    private String ionName;

    @JsonProperty("nombreIUPAC")
    private String nameIUPAC;

    @JsonProperty("cargaIon")
    private Integer ionCharge;

    @JsonProperty("radioIonico")
    private Float ionRadius;

    @JsonProperty("limiteVertido")
    private Float dischargeLimit;


    public AdsorbateResponse() {
    }

    public AdsorbateResponse(Adsorbate adsorbate) {
        this.id = adsorbate.getId();
        this.ionCharge = adsorbate.getIonCharge();
        this.nameIUPAC = adsorbate.getNameIUPAC();
        this.ionName= adsorbate.getIonName();
        this.ionRadius = adsorbate.getIonRadius();
        this.dischargeLimit = adsorbate.getDischargeLimit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIonName() {
        return ionName;
    }

    public void setIonName(String ionName) {
        this.ionName = ionName;
    }

    public String getNameIUPAC() {
        return nameIUPAC;
    }

    public void setNameIUPAC(String nameIUPAC) {
        this.nameIUPAC = nameIUPAC;
    }

    public Integer getIonCharge() {
        return ionCharge;
    }

    public void setIonCharge(Integer ionCharge) {
        this.ionCharge = ionCharge;
    }

    public Float getIonRadius() {
        return ionRadius;
    }

    public void setIonRadius(Float ionRadius) {
        this.ionRadius = ionRadius;
    }

    public Float getDischargeLimit() {
        return dischargeLimit;
    }

    public void setDischargeLimit(Float dischargeLimit) {
        this.dischargeLimit = dischargeLimit;
    }
}
