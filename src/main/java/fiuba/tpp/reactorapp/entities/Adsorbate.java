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
    private String IUPACName;
    private String IUPACNameNormalized;
    private Integer ionCharge;
    private Float ionRadius;
    private Float dischargeLimit;

    public Adsorbate() {
    }

    public Adsorbate(String ionName, String IUPACName, Integer ionCharge, Float ionRadius, Float dischargeLimit) {
        this.ionName = ionName;
        this.IUPACName = IUPACName;
        this.ionCharge = ionCharge;
        this.ionRadius = ionRadius;
        this.dischargeLimit = dischargeLimit;
    }

    public Adsorbate(AdsorbateRequest adsorbato) {
        copyData(adsorbato);
    }

    public Adsorbate update(AdsorbateRequest adsorbato){
        copyData(adsorbato);
        return this;
    }

    private void copyData(AdsorbateRequest adsorbato){
        this.ionName = adsorbato.getNombreIon();
        this.IUPACName = adsorbato.getNombreIUPAC();
        this.ionCharge = adsorbato.getCargaIon();
        this.ionRadius = adsorbato.getRadioIonico();
        this.dischargeLimit = adsorbato.getLimiteVertido();
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

    public void setIonName(String nombreIon) {
        this.ionName = nombreIon;
    }

    public Integer getIonCharge() {
        return ionCharge;
    }

    public void setIonCharge(Integer cargaIon) {
        this.ionCharge = cargaIon;
    }

    public Float getIonRadius() {
        return ionRadius;
    }

    public void setIonRadius(Float radioIonico) {
        this.ionRadius = radioIonico;
    }

    public Float getDischargeLimit() {
        return dischargeLimit;
    }

    public void setDischargeLimit(Float limiteVertido) {
        this.dischargeLimit = limiteVertido;
    }

    public String toString(){
        return ionName + "|" + ionCharge + "|" + ionRadius +"|" + dischargeLimit;
    }

    public String getIUPACName() {
        return IUPACName;
    }

    public void setIUPACName(String nombreIUPAC) {
        this.IUPACName = nombreIUPAC;
    }

    public String getIonNameNormalized() {
        return ionNameNormalized;
    }

    public void setIonNameNormalized(String nombreIonNormalizado) {
        this.ionNameNormalized = nombreIonNormalizado;
    }

    public String getIUPACNameNormalized() {
        return IUPACNameNormalized;
    }

    public void setIUPACNameNormalized(String nombreIUPACNormalizado) {
        this.IUPACNameNormalized = nombreIUPACNormalizado;
    }

    @PreUpdate
    @PrePersist
    protected void normalize() {
        ionNameNormalized = (ionName == null)? "" : StringUtils.stripAccents(ionName.toLowerCase());
        IUPACNameNormalized = (IUPACName == null)? "" : StringUtils.stripAccents(IUPACName.toLowerCase());
    }
}
