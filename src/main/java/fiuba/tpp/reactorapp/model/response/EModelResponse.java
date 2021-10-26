package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.EModel;

public class EModelResponse {

    @JsonProperty("nombreVerbose")
    private String name;

    @JsonProperty("modelo")
    private String model;

    public EModelResponse() {
    }

    public EModelResponse(EModel eModel) {
        this.name = eModel.getModelName();
        this.model = eModel.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
