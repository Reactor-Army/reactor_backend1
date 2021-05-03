package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name ="ADSORBATO")
public class Adsorbato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreIon;
    private String nombreIonNormalizado;
    private String nombreIUPAC;
    private String nombreIUPACNormalizado;
    private Integer cargaIon;
    private Float radioIonico;
    private Float limiteVertido;

    public Adsorbato() {
    }

    public Adsorbato(String nombreIon, String nombreIUPAC,Integer cargaIon, Float radioIonico, Float limiteVertido) {
        this.nombreIon = nombreIon;
        this.nombreIUPAC = nombreIUPAC;
        this.cargaIon = cargaIon;
        this.radioIonico = radioIonico;
        this.limiteVertido = limiteVertido;
    }

    public Adsorbato(AdsorbatoRequest adsorbato) {
        copyData(adsorbato);
    }

    public Adsorbato update(AdsorbatoRequest adsorbato){
        copyData(adsorbato);
        return this;
    }

    private void copyData(AdsorbatoRequest adsorbato){
        this.nombreIon = adsorbato.getNombreIon();
        this.nombreIUPAC = adsorbato.getNombreIUPAC();
        this.cargaIon = adsorbato.getCargaIon();
        this.radioIonico = adsorbato.getRadioIonico();
        this.limiteVertido = adsorbato.getLimiteVertido();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreIon() {
        return nombreIon;
    }

    public void setNombreIon(String nombreIon) {
        this.nombreIon = nombreIon;
    }

    public Integer getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Integer cargaIon) {
        this.cargaIon = cargaIon;
    }

    public Float getRadioIonico() {
        return radioIonico;
    }

    public void setRadioIonico(Float radioIonico) {
        this.radioIonico = radioIonico;
    }

    public Float getLimiteVertido() {
        return limiteVertido;
    }

    public void setLimiteVertido(Float limiteVertido) {
        this.limiteVertido = limiteVertido;
    }

    public String toString(){
        return nombreIon + "|" + cargaIon  + "|" + radioIonico +"|" + limiteVertido;
    }

    public String getNombreIUPAC() {
        return nombreIUPAC;
    }

    public void setNombreIUPAC(String nombreIUPAC) {
        this.nombreIUPAC = nombreIUPAC;
    }

    public String getNombreIonNormalizado() {
        return nombreIonNormalizado;
    }

    public void setNombreIonNormalizado(String nombreIonNormalizado) {
        this.nombreIonNormalizado = nombreIonNormalizado;
    }

    public String getNombreIUPACNormalizado() {
        return nombreIUPACNormalizado;
    }

    public void setNombreIUPACNormalizado(String nombreIUPACNormalizado) {
        this.nombreIUPACNormalizado = nombreIUPACNormalizado;
    }

    @PreUpdate
    @PrePersist
    protected void normalize() {
        nombreIonNormalizado = (nombreIon == null)? "" : StringUtils.stripAccents(nombreIon.toLowerCase());
        nombreIUPACNormalizado= (nombreIUPAC == null)? "" : StringUtils.stripAccents(nombreIUPAC.toLowerCase());
    }
}
