package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    private String impurities;

    private String sampleOrigin;

    private String formula;

    private String speciesName;

    @Column(columnDefinition = "boolean default false")
    private Boolean free;

    @Column(length = 2000)
    @Size(max = 2000)
    private String observations;


    public Adsorbent() {}

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
        this.impurities = request.getImpurities();
        this.formula = request.getFormula();
        this.sampleOrigin = request.getSampleOrigin();
        this.speciesName = request.getSpeciesName();
        this.observations = request.getObservations();
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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean isFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    @PreUpdate
    @PrePersist
    protected void normalize() {
        nameNormalized = (name == null)? "" : StringUtils.stripAccents(name.toLowerCase());
    }

}

