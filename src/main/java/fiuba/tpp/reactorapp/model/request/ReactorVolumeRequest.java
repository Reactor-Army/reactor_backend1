package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorVolumeRequest {

    @JsonProperty("concentracionInicial")
    private Double initialConcentration;

    @JsonProperty("concentracionFinal")
    private Double finalConcentration;

    @JsonProperty("caudal")
    private Double flow;

    public ReactorVolumeRequest(Double initialConcentration, Double finalConcentration, Double flow) {
        this.initialConcentration = initialConcentration;
        this.finalConcentration = finalConcentration;
        this.flow = flow;
    }

    public Double getInitialConcentration() {
        return initialConcentration;
    }

    public void setInitialConcentration(Double initialConcentration) {
        this.initialConcentration = initialConcentration;
    }

    public Double getFinalConcentration() {
        return finalConcentration;
    }

    public void setFinalConcentration(Double finalConcentration) {
        this.finalConcentration = finalConcentration;
    }

    public Double getFlow() {
        return flow;
    }

    public void setFlow(Double flow) {
        this.flow = flow;
    }
}
