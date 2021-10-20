package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BreakCurveDataRequest {

    @JsonProperty("sistemaId")
    private Long processId;

    @JsonProperty("nombre")
    private String name;

    public BreakCurveDataRequest() {
    }

    public BreakCurveDataRequest(Long processId, String name) {
        this.processId = processId;
        this.name = name;
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
}
