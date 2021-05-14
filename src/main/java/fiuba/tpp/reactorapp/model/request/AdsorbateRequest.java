package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdsorbateRequest {

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

    @JsonProperty("numeroCAS")
    private String numberCAS;

    @JsonProperty("formula")
    private String formula;

    public AdsorbateRequest() {
    }

    public AdsorbateRequest(String ionName, String nameIUPAC, Integer ionCharge, Float ionRadius, Float dischargeLimit) {
        this.ionName = ionName;
        this.nameIUPAC = nameIUPAC;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
    }

    public AdsorbateRequest(String ionName, String nameIUPAC, Integer ionCharge, Float ionRadius, Float dischargeLimit, String numberCAS, String formula) {
        this.ionName = ionName;
        this.nameIUPAC = nameIUPAC;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
        this.numberCAS = numberCAS;
        this.formula = formula;
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

    public String getNumberCAS() {
        return numberCAS;
    }

    public void setNumberCAS(String numberCAS) {
        this.numberCAS = numberCAS;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
