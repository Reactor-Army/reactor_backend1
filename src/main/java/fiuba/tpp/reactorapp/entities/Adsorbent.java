package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;

import javax.persistence.*;

@Entity
@Table(name ="ADSORBENT")
public class Adsorbent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String particleSize;

    private Float sBet;

    private Float vBet;

    private Float pHZeroCharge;


    public Adsorbent() {
    }

    public Adsorbent(String name, String particleSize, Float sBet, Float vBet, Float pHZeroCharge) {
        this.name = name;
        this.particleSize = particleSize;
        this.sBet = sBet;
        this.vBet = vBet;
        this.pHZeroCharge = pHZeroCharge;
    }

    public Adsorbent(AdsorbentRequest request){
        copyData(request);
    }

    public Adsorbent update(AdsorbentRequest request){
        copyData(request);
        return this;
    }

    private void copyData(AdsorbentRequest request){
        this.name = request.getNombre();
        this.particleSize = request.getParticulaT();
        this.sBet = request.getsBet();
        this.vBet = request.getvBet();
        this.pHZeroCharge = request.getpHCargaCero();
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

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(String particulaT) {
        this.particleSize = particulaT;
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

    public Float getpPHZeroCharge() {
        return pHZeroCharge;
    }

    public void setpPHZeroCharge(Float pHCargaCero) {
        this.pHZeroCharge = pHCargaCero;
    }

    public String toString() {
        return name + "|" + particleSize + "|" + sBet + "|" + vBet + "|" + pHZeroCharge;
    }
}

