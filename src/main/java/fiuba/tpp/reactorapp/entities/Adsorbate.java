package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name ="ADSORBATE")
public class Adsorbate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ionName;
    private String ionNameNormalized;
    private String nameIUPAC;
    private String nameIUPACNormalized;
    private Integer ionCharge;
    private Float ionRadius;
    private Float dischargeLimit;

    public Adsorbate() {
    }

    public Adsorbate(String ionName, String nameIUPAC, Integer ionCharge, Float ionRadius, Float dischargeLimit) {
        this.ionName = ionName;
        this.nameIUPAC = nameIUPAC;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
    }

    public Adsorbate(AdsorbateRequest adsorbate) {
        copyData(adsorbate);
    }

    public Adsorbate update(AdsorbateRequest adsorbate){
        copyData(adsorbate);
        return this;
    }

    private void copyData(AdsorbateRequest adsorbate){
        this.ionName = adsorbate.getNombreIon();
        this.nameIUPAC = adsorbate.getNombreIUPAC();
        this.ionCharge = adsorbate.getCargaIon();
        this.ionRadius = adsorbate.getRadioIonico();
        this.dischargeLimit = adsorbate.getLimiteVertido();
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

    public String getIonNameNormalized() {
        return ionNameNormalized;
    }

    public void setIonNameNormalized(String ionNameNormalized) {
        this.ionNameNormalized = ionNameNormalized;
    }

    public String getNameIUPAC() {
        return nameIUPAC;
    }

    public void setNameIUPAC(String nameIUPAC) {
        this.nameIUPAC = nameIUPAC;
    }

    public String getNameIUPACNormalized() {
        return nameIUPACNormalized;
    }

    public void setNameIUPACNormalized(String nameIUPACNormalized) {
        this.nameIUPACNormalized = nameIUPACNormalized;
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

    @PreUpdate
    @PrePersist
    protected void normalize() {
        ionNameNormalized = (ionName == null)? "" : StringUtils.stripAccents(ionName.toLowerCase());
        nameIUPACNormalized = (nameIUPAC == null)? "" : StringUtils.stripAccents(nameIUPAC.toLowerCase());
    }
}
