package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorVolumeResponse {

    @JsonProperty("proceso")
    private ProcessResponse process;

    @JsonProperty("volumen")
    private Float volume;

    public ReactorVolumeResponse(ProcessResponse process, Float volume) {
        this.process = process;
        this.volume = volume;
    }

    public ProcessResponse getProcess() {
        return process;
    }

    public void setProcess(ProcessResponse process) {
        this.process = process;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }
}
