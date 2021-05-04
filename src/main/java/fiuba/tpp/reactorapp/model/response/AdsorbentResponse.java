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

    public AdsorbentResponse() {
    }

    public AdsorbentResponse(Adsorbent adsorbent) {
        this.id = adsorbent.getId();
        this.name = adsorbent.getName();
        this.particleSize = adsorbent.getParticleSize();
        this.sBet = adsorbent.getsBet();
        this.vBet = adsorbent.getvBet();
        this.pHZeroCharge = adsorbent.getpHZeroCharge();
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
