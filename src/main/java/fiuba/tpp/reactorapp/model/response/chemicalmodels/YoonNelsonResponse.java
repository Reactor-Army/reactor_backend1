package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.List;

public class YoonNelsonResponse {

    @JsonProperty("constanteYoonNelson")
    private double yoonNelsonConstant;

    @JsonProperty("tiempoCincuentaPorciento")
    private double timeFiftyPercent;

    @JsonProperty("observaciones")
    private List<Observation> observations;

    public YoonNelsonResponse(double yoonNelsonConstant, double timeFiftyPercent) {
        this.yoonNelsonConstant = yoonNelsonConstant;
        this.timeFiftyPercent = timeFiftyPercent;
    }

    public double getYoonNelsonConstant() {
        return yoonNelsonConstant;
    }

    public void setYoonNelsonConstant(double yoonNelsonConstant) {
        this.yoonNelsonConstant = yoonNelsonConstant;
    }

    public double getTimeFiftyPercent() {
        return timeFiftyPercent;
    }

    public void setTimeFiftyPercent(double timeFiftyPercent) {
        this.timeFiftyPercent = timeFiftyPercent;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
