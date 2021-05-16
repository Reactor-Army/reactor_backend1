package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.dto.SearchByAdsorbateDTO;

import java.util.ArrayList;
import java.util.List;

public class SearchByAdsorbateResponse {

    @JsonProperty("adsorbente")
    private AdsorbentResponse adsorbent;

    @JsonProperty("maxQmax")
    private Float maxQmax;

    @JsonProperty("remueveTodosLosAdsorbatos")
    private boolean removesAllAdsorbates;

    @JsonProperty("procesos")
    private List<ProcessResponse> processes;

    public SearchByAdsorbateResponse() {
    }

    public SearchByAdsorbateResponse(SearchByAdsorbateDTO result, Integer numberOfAdsorbates) {
        this.processes = new ArrayList<>();
        for (Process process: result.getProcesses()) {
            processes.add(new ProcessResponse(process));
        }
        this.adsorbent = new AdsorbentResponse(result.getProcesses().get(0).getAdsorbent());
        this.maxQmax = result.getMaxQmax();
        this.removesAllAdsorbates = result.hasAllAdsorbates(numberOfAdsorbates);
    }

    public List<ProcessResponse> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessResponse> processes) {
        this.processes = processes;
    }

    public Float getMaxQmax() {
        return maxQmax;
    }

    public void setMaxQmax(Float maxQmax) {
        this.maxQmax = maxQmax;
    }

    public boolean isRemovesAllAdsorbates() {
        return removesAllAdsorbates;
    }

    public void setRemovesAllAdsorbates(boolean removesAllAdsorbates) {
        this.removesAllAdsorbates = removesAllAdsorbates;
    }

    public AdsorbentResponse getAdsorbent() {
        return adsorbent;
    }

    public void setAdsorbent(AdsorbentResponse adsorbent) {
        this.adsorbent = adsorbent;
    }
}
