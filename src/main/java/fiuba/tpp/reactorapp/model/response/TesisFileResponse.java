package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.entities.TesisFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TesisFileResponse {

    private Long id;

    @JsonProperty("titulo")
    private String name;

    @JsonProperty("formato")
    private String type;

    @JsonProperty("fechaPublicacion")
    private Date publicationDate;

    @JsonProperty("sistemasRelacionados ")
    private List<ProcessResponse> processes;

    public TesisFileResponse() {
    }

    public TesisFileResponse(TesisFile tesisFile) {
        copyData(tesisFile);
    }

    private void copyData(TesisFile tesisFile){
        this.id = tesisFile.getId();
        this.name = tesisFile.getName();
        this.publicationDate = tesisFile.getPublicationDate();
        this.type = tesisFile.getType();
        this.processes = new ArrayList<>();
        for (Process p: tesisFile.getProcesses()) {
            processes.add(new ProcessResponse(p));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProcessResponse> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessResponse> processes) {
        this.processes = processes;
    }
}