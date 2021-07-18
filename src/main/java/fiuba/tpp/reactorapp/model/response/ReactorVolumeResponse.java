package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorVolumeResponse {

    @JsonProperty("proceso")
    private ProcessResponse process;

    @JsonProperty("volumen")
    private double volume;

    public ReactorVolumeResponse(ProcessResponse process, double volume) {
        this.process = process;
        this.volume = volume;
    }

    public ProcessResponse getProcess() {
        return process;
    }

    public void setProcess(ProcessResponse process) {
        this.process = process;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
