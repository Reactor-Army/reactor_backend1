package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessCountResponse {

    @JsonProperty("cantidadProcesos")
    private Integer processCount;

    public ProcessCountResponse(Integer processCount) {
        this.processCount = processCount;
    }

    public Integer getProcessCount() {
        return processCount;
    }

    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }
}
