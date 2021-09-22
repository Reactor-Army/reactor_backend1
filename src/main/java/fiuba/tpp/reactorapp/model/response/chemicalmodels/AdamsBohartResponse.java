package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdamsBohartResponse extends ModelResponse {

    @JsonProperty("constanteAdamsBohart")
    private double adamsBohartConstant;

    @JsonProperty("capacidadMaximaAbsorcion")
    private double maxAbsorptionCapacity;

    public AdamsBohartResponse(double adamsBohartConstant, double maxAbsorptionCapacity) {
        super();
        this.adamsBohartConstant = adamsBohartConstant;
        this.maxAbsorptionCapacity = maxAbsorptionCapacity;
    }

    public double getAdamsBohartConstant() {
        return adamsBohartConstant;
    }

    public void setAdamsBohartConstant(double adamsBohartConstant) {
        this.adamsBohartConstant = adamsBohartConstant;
    }

    public double getMaxAbsorptionCapacity() {
        return maxAbsorptionCapacity;
    }

    public void setMaxAbsorptionCapacity(double maxAbsorptionCapacity) {
        this.maxAbsorptionCapacity = maxAbsorptionCapacity;
    }
}
