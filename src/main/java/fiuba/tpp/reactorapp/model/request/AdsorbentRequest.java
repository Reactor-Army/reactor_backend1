package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdsorbentRequest {

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

    public AdsorbentRequest() {
    }

    public AdsorbentRequest(String name, String particleSize, Float sBet, Float vBet, Float pHZeroCharge) {
        this.name = name;
        this.particleSize = particleSize;
        this.sBet = sBet;
        this.vBet = vBet;
        this.pHZeroCharge = pHZeroCharge;
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
}
