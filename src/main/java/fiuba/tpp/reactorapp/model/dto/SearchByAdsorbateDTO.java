package fiuba.tpp.reactorapp.model.dto;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Process;

import java.util.ArrayList;
import java.util.List;

public class SearchByAdsorbateDTO {

    private List<Process> processes;

    private List<Adsorbate> removeAdsorbates;

    private float maxQmax;

    public SearchByAdsorbateDTO(Process process) {
        this.processes = new ArrayList<>();
        this.removeAdsorbates = new ArrayList<>();
        processes.add(process);
        this.removeAdsorbates.add(process.getAdsorbate());
        maxQmax = process.getQmax();
    }

    public boolean hasAllAdsorbates(Integer numberOfAdsorbates){
        return removeAdsorbates.size() == numberOfAdsorbates;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public float getMaxQmax() {
        return maxQmax;
    }

    public void setMaxQmax(float maxQmax) {
        this.maxQmax = maxQmax;
    }

    public List<Adsorbate> getRemoveAdsorbates() {
        return removeAdsorbates;
    }

    public void setRemoveAdsorbates(List<Adsorbate> removeAdsorbates) {
        this.removeAdsorbates = removeAdsorbates;
    }
}
