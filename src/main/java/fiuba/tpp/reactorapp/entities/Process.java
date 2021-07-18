package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name ="PROCESS")
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Adsorbate adsorbate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @Positive
    private Float kineticConstant;

    @Range(min = 1, max = 2)
    private Integer reactionOrder;


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
        this.equilibriumTime = request.getEquilibriumTime();
        this.temperature = request.getTemperature();
        this.initialPH = request.getInitialPH();
        this.complexation = request.isComplexation();
        this.ionicInterchange = request.isIonicInterchange();
        this.chemicalReaction = request.isChemicalReaction();
        this.observation = request.getObservation();
        this.source = request.getSource();
        this.kineticConstant = request.getKineticConstant();
        this.reactionOrder = request.getReactionOrder();
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

    public Float getQmax() {
        return qmax;
    }

    public void setQmax(Float qmax) {
        this.qmax = qmax;
    }

    public Float getEquilibriumTime() {
        return equilibriumTime;
    }

    public void setEquilibriumTime(Float equilibriumTime) {
        this.equilibriumTime = equilibriumTime;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getInitialPH() {
        return initialPH;
    }

    public void setInitialPH(Float initialPH) {
        this.initialPH = initialPH;
    }

    public boolean isComplexation() {
        return complexation;
    }

    public void setComplexation(boolean complexation) {
        this.complexation = complexation;
    }

    public boolean isIonicInterchange() {
        return ionicInterchange;
    }

    public void setIonicInterchange(boolean ionicInterchange) {
        this.ionicInterchange = ionicInterchange;
    }

    public boolean isChemicalReaction() {
        return chemicalReaction;
    }

    public void setChemicalReaction(boolean chemicalReaction) {
        this.chemicalReaction = chemicalReaction;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getKineticConstant() {
        return kineticConstant;
    }

    public void setKineticConstant(Float kineticConstant) {
        this.kineticConstant = kineticConstant;
    }

    public Integer getReactionOrder() {
        return reactionOrder;
    }

    public void setReactionOrder(Integer reactionOrder) {
        this.reactionOrder = reactionOrder;
    }

    @Override
    public String toString() {
        return "Reactor{" +
                "adsorbate=" + adsorbate.getIonName() +
                ", adsorbent=" + adsorbent.getName() +
                ", qMax=" + qmax +
                ", equilibriumTime=" + equilibriumTime +
                ", temperature=" + temperature +
                ", initialPH=" + initialPH +
                ", complexation=" + complexation +
                ", ionicInterchange=" + ionicInterchange +
                ", chemicalReaction=" + chemicalReaction +
                ", observation='" + observation + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
