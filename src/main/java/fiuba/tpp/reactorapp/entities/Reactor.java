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
    private Adsorbate adsorbate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Adsorbent adsorbent;

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


    public Reactor(Adsorbate adsorbate, Adsorbent adsorbent, ReactorRequest request){
        copyData(adsorbate, adsorbent,request);
    }

    public Reactor update (Adsorbate adsorbate, Adsorbent adsorbent, ReactorRequest request){
        copyData(adsorbate, adsorbent,request);
        return this;
    }

    private void copyData(Adsorbate adsorbate, Adsorbent adsorbent, ReactorRequest request){
        this.adsorbate = adsorbate;
        this.adsorbent = adsorbent;
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

    public Adsorbate getAdsorbate() {
        return adsorbate;
    }

    public void setAdsorbate(Adsorbate adsorbate) {
        this.adsorbate = adsorbate;
    }

    public Adsorbent getAdsorbent() {
        return adsorbent;
    }

    public void setAdsorbent(Adsorbent adsorbent) {
        this.adsorbent = adsorbent;
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
                "adsorbate=" + adsorbate.getNombreIon() +
                ", adsorbente=" + adsorbent.getNombre() +
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
