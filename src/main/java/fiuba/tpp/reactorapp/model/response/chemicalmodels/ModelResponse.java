package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.ArrayList;
import java.util.List;

public class ModelResponse {

    @JsonProperty("R2")
    private double rms;

    @JsonProperty("observaciones")
    private List<Observation> observations;

    public ModelResponse() {
        observations = new ArrayList<>();
    }

    public double getRms() {
        return rms;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

}
