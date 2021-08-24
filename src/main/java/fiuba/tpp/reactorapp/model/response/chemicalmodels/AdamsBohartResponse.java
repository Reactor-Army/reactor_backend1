package fiuba.tpp.reactorapp.model.response.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.model.math.Observation;

import java.util.List;

public class AdamsBohartResponse {

    @JsonProperty("constanteAdamsBohart")
    private double adamsBohartConstant;

    @JsonProperty("capacidadMaximaAbsorcion")
    private double maxAbsortionCapacity;

    @JsonProperty("observaciones")
    private List<Observation> observations;

    public AdamsBohartResponse(double adamsBohartConstant, double maxAbsortionCapacity) {
        this.adamsBohartConstant = adamsBohartConstant;
        this.maxAbsortionCapacity = maxAbsortionCapacity;
    }

    public double getAdamsBohartConstant() {
        return adamsBohartConstant;
    }

    public void setAdamsBohartConstant(double adamsBohartConstant) {
        this.adamsBohartConstant = adamsBohartConstant;
    }

    public double getMaxAbsortionCapacity() {
        return maxAbsortionCapacity;
    }

    public void setMaxAbsortionCapacity(double maxAbsortionCapacity) {
        this.maxAbsortionCapacity = maxAbsortionCapacity;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
