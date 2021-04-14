package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;

import javax.persistence.*;

@Entity
@Table(name ="ADSORBATO")
public class Adsorbato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreIon;
    private Float cargaIon;
    private Float radioIonico;
    private Float limiteVertido;

    public Adsorbato() {
    }

    public Adsorbato(String nombreIon, Float cargaIon, Float radioIonico, Float limiteVertido) {
        this.nombreIon = nombreIon;
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

    public Float getCargaIon() {
        return cargaIon;
    }

    public void setCargaIon(Float cargaIon) {
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
}
