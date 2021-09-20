package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.List;

public class YoonNelsonResponse extends ModelResponse {

    @JsonProperty("constanteYoonNelson")
    private double yoonNelsonConstant;

    @JsonProperty("tiempoCincuentaPorciento")
    private double timeFiftyPercent;


    public YoonNelsonResponse(double yoonNelsonConstant, double timeFiftyPercent) {
        super();
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

}
