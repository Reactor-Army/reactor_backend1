package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Adsorbent;

public class AdsorbentResponse {

    private Long id;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("particulaT")
    private String particleSize;

    @JsonProperty("sBet")
    private Float sBet;

    @JsonProperty("vBet")
    private Float vBet;

    @JsonProperty("pHCargaCero")
    private Float pHZeroCharge;

    @JsonProperty("impurezas")
    private String impurities;

    @JsonProperty("origenMuestra")
    private String sampleOrigin;

    @JsonProperty("formula")
    private String formula;

    @JsonProperty("nombreEspecie")
    private String speciesName;

    public AdsorbentResponse() {
    }

    public AdsorbentResponse(Adsorbent adsorbent) {
        this.id = adsorbent.getId();
        this.name = adsorbent.getName();
        this.particleSize = adsorbent.getParticleSize();
        this.sBet = adsorbent.getsBet();
        this.vBet = adsorbent.getvBet();
        this.pHZeroCharge = adsorbent.getpHZeroCharge();
        this.sampleOrigin = adsorbent.getSampleOrigin();
        this.impurities = adsorbent.getImpurities();
        this.formula = adsorbent.getFormula();
        this.speciesName = adsorbent.getSpeciesName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(String particleSize) {
        this.particleSize = particleSize;
    }

    public Float getsBet() {
        return sBet;
    }

    public void setsBet(Float sBet) {
        this.sBet = sBet;
    }

    public Float getvBet() {
        return vBet;
    }

    public void setvBet(Float vBet) {
        this.vBet = vBet;
    }

    public Float getpHZeroCharge() {
        return pHZeroCharge;
    }

    public void setpHZeroCharge(Float pHZeroCharge) {
        this.pHZeroCharge = pHZeroCharge;
    }

    public String getImpurities() {
        return impurities;
    }

    public void setImpurities(String impurities) {
        this.impurities = impurities;
    }

    public String getSampleOrigin() {
        return sampleOrigin;
    }

    public void setSampleOrigin(String sampleOrigin) {
        this.sampleOrigin = sampleOrigin;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }
}
