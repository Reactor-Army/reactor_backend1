package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.ProcessRequest;

import javax.persistence.*;

@Entity
@Table(name ="PROCESS")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Adsorbate adsorbate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Adsorbent adsorbent;

    private Float qmax;

    private Float equilibriumTime;

    private Float temperature;

    private Float initialPH;

    private boolean complexation;

    private boolean ionicInterchange;

    private boolean chemicalReaction;

    private String observation;

    private String source;


    public Process() {
    }


    public Process(Adsorbate adsorbate, Adsorbent adsorbent, ProcessRequest request){
        copyData(adsorbate, adsorbent,request);
    }

    public Process update (Adsorbate adsorbate, Adsorbent adsorbent, ProcessRequest request){
        copyData(adsorbate, adsorbent,request);
        return this;
    }

    private void copyData(Adsorbate adsorbate, Adsorbent adsorbent, ProcessRequest request){
        this.adsorbate = adsorbate;
        this.adsorbent = adsorbent;
        this.qmax = request.getQmax();
        this.equilibriumTime = request.getTiempoEquilibrio();
        this.temperature = request.getTemperatura();
        this.initialPH = request.getPhinicial();
        this.complexation = request.isComplejacion();
        this.ionicInterchange = request.isIntercambioIonico();
        this.chemicalReaction = request.isReaccionQuimica();
        this.observation = request.getObservacion();
        this.source = request.getFuente();
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

    public Float getEquilibriumTime() {
        return equilibriumTime;
    }

    public void setEquilibriumTime(Float tiempoEquilibrio) {
        this.equilibriumTime = tiempoEquilibrio;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperatura) {
        this.temperature = temperatura;
    }

    public Float getQmax() {
        return qmax;
    }

    public void setQmax(Float qmax) {
        this.qmax = qmax;
    }

    public Float getInitialPH() {
        return initialPH;
    }

    public void setInitialPH(Float phinicial) {
        this.initialPH = phinicial;
    }

    public boolean isComplexation() {
        return complexation;
    }

    public void setComplexation(boolean complejacion) {
        this.complexation = complejacion;
    }

    public boolean isIonicInterchange() {
        return ionicInterchange;
    }

    public void setIonicInterchange(boolean intercambioIonico) {
        this.ionicInterchange = intercambioIonico;
    }

    public boolean isChemicalReaction() {
        return chemicalReaction;
    }

    public void setChemicalReaction(boolean reaccionQuimica) {
        this.chemicalReaction = reaccionQuimica;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observacion) {
        this.observation = observacion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String fuente) {
        this.source = fuente;
    }

    @Override
    public String toString() {
        return "Reactor{" +
                "adsorbate=" + adsorbate.getIonName() +
                ", adsorbente=" + adsorbent.getName() +
                ", qMax=" + qmax +
                ", tiempoEquilibrio=" + equilibriumTime +
                ", temperatura=" + temperature +
                ", pHInicial=" + initialPH +
                ", complejacion=" + complexation +
                ", intercambioIonico=" + ionicInterchange +
                ", reaccionQuimica=" + chemicalReaction +
                ", observacion='" + observation + '\'' +
                ", fuente='" + source + '\'' +
                '}';
    }
}
