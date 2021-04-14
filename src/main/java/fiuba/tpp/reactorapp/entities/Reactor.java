package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.ReactorRequest;

import javax.persistence.*;

@Entity
@Table(name ="REACTOR")
public class Reactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Adsorbato adsorbato;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Adsorbente adsorbente;

    private Float qmax;

    private Float tiempoEquilibrio;

    private Float temperatura;

    private Float phinicial;

    private boolean complejacion;

    private boolean intercambioIonico;

    private boolean reaccionQuimica;

    private String observacion;

    private String fuente;


    public Reactor() {
    }

    public Reactor(Adsorbato adsorbato, Adsorbente adsorbente, Float qMax, Float tiempoEquilibrio, Float temperatura, Float pHInicial, boolean complejacion, boolean intercambioIonico, boolean reaccionQuimica, String observacion, String fuente) {
        this.adsorbato = adsorbato;
        this.adsorbente = adsorbente;
        this.qmax = qMax;
        this.tiempoEquilibrio = tiempoEquilibrio;
        this.temperatura = temperatura;
        this.phinicial = pHInicial;
        this.complejacion = complejacion;
        this.intercambioIonico = intercambioIonico;
        this.reaccionQuimica = reaccionQuimica;
        this.observacion = observacion;
        this.fuente = fuente;
    }

    public Reactor(Adsorbato adsorbato, Adsorbente adsorbente, ReactorRequest request){
        copyData(adsorbato,adsorbente,request);
    }

    public Reactor update (Adsorbato adsorbato, Adsorbente adsorbente, ReactorRequest request){
        copyData(adsorbato,adsorbente,request);
        return this;
    }

    private void copyData(Adsorbato adsorbato, Adsorbente adsorbente, ReactorRequest request){
        this.adsorbato = adsorbato;
        this.adsorbente = adsorbente;
        this.qmax = request.getQmax();
        this.tiempoEquilibrio = request.getTiempoEquilibrio();
        this.temperatura = request.getTemperatura();
        this.phinicial = request.getPhinicial();
        this.complejacion = request.isComplejacion();
        this.intercambioIonico = request.isIntercambioIonico();
        this.reaccionQuimica = request.isReaccionQuimica();
        this.observacion = request.getObservacion();
        this.fuente = request.getFuente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adsorbato getAdsorbato() {
        return adsorbato;
    }

    public void setAdsorbato(Adsorbato adsorbato) {
        this.adsorbato = adsorbato;
    }

    public Adsorbente getAdsorbente() {
        return adsorbente;
    }

    public void setAdsorbente(Adsorbente adsorbente) {
        this.adsorbente = adsorbente;
    }


    public Float getTiempoEquilibrio() {
        return tiempoEquilibrio;
    }

    public void setTiempoEquilibrio(Float tiempoEquilibrio) {
        this.tiempoEquilibrio = tiempoEquilibrio;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getQmax() {
        return qmax;
    }

    public void setQmax(Float qmax) {
        this.qmax = qmax;
    }

    public Float getPhinicial() {
        return phinicial;
    }

    public void setPhinicial(Float phinicial) {
        this.phinicial = phinicial;
    }

    public boolean isComplejacion() {
        return complejacion;
    }

    public void setComplejacion(boolean complejacion) {
        this.complejacion = complejacion;
    }

    public boolean isIntercambioIonico() {
        return intercambioIonico;
    }

    public void setIntercambioIonico(boolean intercambioIonico) {
        this.intercambioIonico = intercambioIonico;
    }

    public boolean isReaccionQuimica() {
        return reaccionQuimica;
    }

    public void setReaccionQuimica(boolean reaccionQuimica) {
        this.reaccionQuimica = reaccionQuimica;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    @Override
    public String toString() {
        return "Reactor{" +
                "adsorbato=" + adsorbato.getNombreIon() +
                ", adsorbente=" + adsorbente.getNombre() +
                ", qMax=" + qmax +
                ", tiempoEquilibrio=" + tiempoEquilibrio +
                ", temperatura=" + temperatura +
                ", pHInicial=" + phinicial +
                ", complejacion=" + complejacion +
                ", intercambioIonico=" + intercambioIonico +
                ", reaccionQuimica=" + reaccionQuimica +
                ", observacion='" + observacion + '\'' +
                ", fuente='" + fuente + '\'' +
                '}';
    }
}
