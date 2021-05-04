package fiuba.tpp.reactorapp.model.response;

import fiuba.tpp.reactorapp.entities.Adsorbent;

public class AdsorbentResponse {

    private Long id;

    private String nombre;

    private String particulaT;

    private Float sBet;

    private Float vBet;

    private Float pHCargaCero;

    public AdsorbentResponse() {
    }

    public AdsorbentResponse(Adsorbent adsorbent) {
        this.id = adsorbent.getId();
        this.nombre = adsorbent.getName();
        this.particulaT = adsorbent.getParticleSize();
        this.sBet = adsorbent.getsBet();
        this.vBet = adsorbent.getvBet();
        this.pHCargaCero = adsorbent.getpHZeroCharge();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getParticulaT() {
        return particulaT;
    }

    public void setParticulaT(String particulaT) {
        this.particulaT = particulaT;
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

    public Float getpHCargaCero() {
        return pHCargaCero;
    }

    public void setpHCargaCero(Float pHCargaCero) {
        this.pHCargaCero = pHCargaCero;
    }
}
