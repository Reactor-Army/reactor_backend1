package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BreakCurveDataRequest {

    @JsonProperty("sistemaId")
    private Long processId;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("esLineaBase")
    private boolean isBaseline;


    public BreakCurveDataRequest() {
    }

    public BreakCurveDataRequest(Long processId, String name, boolean isBaseline) {
        this.processId = processId;
        this.name = name;
        this.isBaseline = isBaseline;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBaseline() {
        return isBaseline;
    }

    public void setBaseline(boolean baseline) {
        isBaseline = baseline;
    }
}
