package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThomasResponse {

    @JsonProperty("constanteThomas")
    private double thomasConstant;

    @JsonProperty("concentracionMaximaSoluto")
    private double maxConcentration;

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
}
