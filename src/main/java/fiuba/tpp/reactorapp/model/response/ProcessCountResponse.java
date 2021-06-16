package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessCountResponse {

    @JsonProperty("cantidadProcesos")
    private Long processCount;

    public ProcessCountResponse(Long processCount) {
        this.processCount = processCount;
    }

    public Long getProcessCount() {
        return processCount;
    }

    public void setProcessCount(Long processCount) {
        this.processCount = processCount;
    }
}
