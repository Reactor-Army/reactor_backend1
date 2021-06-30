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

    @JsonProperty("numeroCAS")
    private String numberCAS;

    @JsonProperty("formula")
    private String formula;

    @JsonProperty("cargaIonFormula")
    private String ionChargeFormula;

    @JsonProperty("masaMolar")
    private Float molarMass;

    @JsonProperty("regulado")
    private Boolean regulated;


    public AdsorbateResponse() {
    }

    public AdsorbateResponse(Adsorbate adsorbate) {
        this.id = adsorbate.getId();
        this.ionCharge = adsorbate.getIonCharge();
        this.nameIUPAC = adsorbate.getNameIUPAC();
        this.ionName= adsorbate.getIonName();
        this.ionRadius = adsorbate.getIonRadius();
        this.dischargeLimit = adsorbate.getDischargeLimit();
        this.numberCAS = adsorbate.getNumberCAS();
        this.formula = adsorbate.getFormula();
        this.ionChargeFormula = adsorbate.getIonChargeText();
        this.molarMass = adsorbate.getMolarMass();
        this.regulated = adsorbate.getRegulated();
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

    public String getIonChargeFormula() {
        return ionChargeFormula;
    }

    public void setIonChargeFormula(String ionChargeFormula) {
        this.ionChargeFormula = ionChargeFormula;
    }

    public Float getMolarMass() {
        return molarMass;
    }

    public void setMolarMass(Float molarMass) {
        this.molarMass = molarMass;
    }
}
