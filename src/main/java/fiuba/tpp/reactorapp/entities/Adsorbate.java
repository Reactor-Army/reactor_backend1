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
    private String ionChargeText;
    private Float ionRadius;
    private Float dischargeLimit;
    private String numberCAS;
    private String formula;

    private static String single_charge = "1";

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
        this.ionName = adsorbate.getIonName();
        this.nameIUPAC = adsorbate.getNameIUPAC();
        this.ionCharge = adsorbate.getIonCharge();
        this.ionRadius = adsorbate.getIonRadius();
        this.dischargeLimit = adsorbate.getDischargeLimit();
        this.formula = adsorbate.getFormula();
        this.numberCAS = adsorbate.getNumberCAS();
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

    public String getIonChargeText() {
        return ionChargeText;
    }

    public void setIonChargeText(String ionChargeText) {
        this.ionChargeText = ionChargeText;
    }

    @PreUpdate
    @PrePersist
    protected void normalize() {
        ionNameNormalized = (ionName == null)? "" : StringUtils.stripAccents(ionName.toLowerCase());
        nameIUPACNormalized = (nameIUPAC == null)? "" : StringUtils.stripAccents(nameIUPAC.toLowerCase());
        ionChargeText = parseIonChargeText(ionCharge);
        if(formula != null && !formula.isEmpty()){
            formula = (formula.contains("+") || formula.contains("-"))?parseFormula(formula,ionChargeText): formula;
        }
    }

    private String parseIonChargeText(Integer ionCharge){
        String text = String.valueOf(ionCharge);
        if(text.length()>1){
            StringBuilder chargeBuilder = new StringBuilder();
            chargeBuilder.append(text);
            chargeBuilder.reverse();
            return chargeBuilder.toString();
        }else{
            return text.concat("+");
        }
    }

    public static String parseFormula(String textFormula, String ionChargeText){
        if(textFormula.contains(ionChargeText)){
            return textFormula.replace(ionChargeText,"");
        }else{
            if(ionChargeText.contains(single_charge)){
                String singleCharge = ionChargeText.replace(single_charge,"");
                return textFormula.replace(singleCharge,"");
            }
            return textFormula;
        }
    }
}
