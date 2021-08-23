package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.List;

public class ThomasResponse {

    @JsonProperty("constanteThomas")
    private double thomasConstant;

    @JsonProperty("concentracionMaximaSoluto")
    private double maxConcentration;

    @JsonProperty("observaciones")
    private List<Observation> observations;

    public ThomasResponse() {
    }

    public ThomasResponse(double thomasConstant, double maxConcentration) {
        this.thomasConstant = thomasConstant;
        this.maxConcentration = maxConcentration;
    }

    public double getThomasConstant() {
        return thomasConstant;
    }

    public void setThomasConstant(double thomasConstant) {
        this.thomasConstant = thomasConstant;
    }

    public double getMaxConcentration() {
        return maxConcentration;
    }

    public void setMaxConcentration(double maxConcentration) {
        this.maxConcentration = maxConcentration;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
