package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Process;

public class ProcessResponse {

    private Long id;

    @JsonProperty("adsorbato")
    private AdsorbateResponse adsorbate;

    @JsonProperty("adsorbente")
    private AdsorbentResponse adsorbent;

    @JsonProperty("qmax")
    private Float qmax;

    @JsonProperty("tiempoEquilibrio")
    private Float equilibriumTime;

    @JsonProperty("temperatura")
    private Float temperature;

    @JsonProperty("phinicial")
    private Float initialPH;

    @JsonProperty("complejacion")
    private boolean complexation;

    @JsonProperty("intercambioIonico")
    private boolean ionicInterchange;

    @JsonProperty("reaccionQuimica")
    private boolean chemicalReaction;

    @JsonProperty("observacion")
    private String observation;

    @JsonProperty("fuente")
    private String source;

    @JsonProperty("constanteCinetica")
    private Float kineticConstant;

    @JsonProperty("ordenReaccion")
    private Float reactionOrder;

    public ProcessResponse() {
    }

    public ProcessResponse(Process process) {
        this.id = process.getId();
        this.adsorbate = new AdsorbateResponse(process.getAdsorbate());
        this.adsorbent = new AdsorbentResponse(process.getAdsorbent());
        this.qmax = process.getQmax();
        this.equilibriumTime = process.getEquilibriumTime();
        this.temperature = process.getTemperature();
        this.initialPH = process.getInitialPH();
        this.complexation = process.isComplexation();
        this.ionicInterchange = process.isIonicInterchange();
        this.chemicalReaction = process.isChemicalReaction();
        this.observation = process.getObservation();
        this.source = process.getSource();
        this.kineticConstant = process.getKineticConstant();
        this.reactionOrder = process.getReactionOrder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdsorbateResponse getAdsorbate() {
        return adsorbate;
    }

    public void setAdsorbate(AdsorbateResponse adsorbate) {
        this.adsorbate = adsorbate;
    }

    public AdsorbentResponse getAdsorbent() {
        return adsorbent;
    }

    public void setAdsorbent(AdsorbentResponse adsorbent) {
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

    public Float getReactionOrder() {
        return reactionOrder;
    }

    public void setReactionOrder(Float reactionOrder) {
        this.reactionOrder = reactionOrder;
    }
}
