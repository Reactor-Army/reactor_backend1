package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdsorbateRequest {

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

    @JsonProperty("masaMolar")
    private Float molarMass;

    @JsonProperty("regulado")
    private Boolean regulated;

    public AdsorbateRequest() {
    }

    public AdsorbateRequest(String ionName, String nameIUPAC, Integer ionCharge, Float ionRadius, Float dischargeLimit) {
        this.ionName = ionName;
        this.nameIUPAC = nameIUPAC;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
    }

    public AdsorbateRequest(String ionName, String nameIUPAC, Integer ionCharge, Float ionRadius, Float dischargeLimit, String formula, Float molarMass) {
        this.ionName = ionName;
        this.nameIUPAC = nameIUPAC;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
        this.formula = formula;
        this.molarMass = molarMass;
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

    public Float getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(Float molarMass) {
        this.molarMass = molarMass;
    }

    public Boolean getRegulated() {
        return regulated;
    }
}
