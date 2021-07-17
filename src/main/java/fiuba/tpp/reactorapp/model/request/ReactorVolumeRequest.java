package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorVolumeRequest {

    @JsonProperty("concentracionInicial")
    private Float initialConcentration;

    @JsonProperty("concentracionFinal")
    private Float finalConcentration;

    @JsonProperty("caudal")
    private Float flow;

    public ReactorVolumeRequest(Float initialConcentration, Float finalConcentration, Float flow) {
        this.initialConcentration = initialConcentration;
        this.finalConcentration = finalConcentration;
        this.flow = flow;
    }

    public Float getInitialConcentration() {
        return initialConcentration;
    }

    public void setInitialConcentration(Float initialConcentration) {
        this.initialConcentration = initialConcentration;
    }

    public Float getFinalConcentration() {
        return finalConcentration;
    }

    public void setFinalConcentration(Float finalConcentration) {
        this.finalConcentration = finalConcentration;
    }

    public Float getFlow() {
        return flow;
    }

    public void setFlow(Float flow) {
        this.flow = flow;
    }
}
