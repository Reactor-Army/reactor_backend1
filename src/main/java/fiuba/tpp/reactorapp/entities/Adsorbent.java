package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name ="ADSORBENT")
public class Adsorbent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nameNormalized;

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
        this.name = request.getName();
        this.particleSize = request.getParticleSize();
        this.sBet = request.getsBet();
        this.vBet = request.getvBet();
        this.pHZeroCharge = request.getpHZeroCharge();
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

    public String getNameNormalized() {
        return nameNormalized;
    }

    public void setNameNormalized(String nameNormalized) {
        this.nameNormalized = nameNormalized;
    }

    @PreUpdate
    @PrePersist
    protected void normalize() {
        nameNormalized = (name == null)? "" : StringUtils.stripAccents(name.toLowerCase());
    }

}

