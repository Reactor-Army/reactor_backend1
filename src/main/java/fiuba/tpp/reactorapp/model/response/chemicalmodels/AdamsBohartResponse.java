package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.List;

public class AdamsBohartResponse {

    @JsonProperty("constanteAdamsBohart")
    private double adamsBohartConstant;

    @JsonProperty("capacidadMaximaAbsorcion")
    private double maxAbsorptionCapacity;

    @JsonProperty("observaciones")
    private List<Observation> observations;

    public AdamsBohartResponse(double adamsBohartConstant, double maxAbsorptionCapacity) {
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

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
