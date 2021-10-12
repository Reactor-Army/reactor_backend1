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

    @JsonProperty("nombreArchivo")
    private String filename;

    @JsonProperty("autor")
    private String author;

    @JsonProperty("formato")
    private String type;

    @JsonProperty("fechaPublicacion")
    private Date publicationDate;

    @JsonProperty("fechaSubida")
    private Date uploadDate;

    @JsonProperty("sistemasRelacionados")
    private List<ProcessResponse> processes;

    public TesisFileResponse() {
    }

    public TesisFileResponse(TesisFile tesisFile) {
        copyData(tesisFile);
    }

    private void copyData(TesisFile tesisFile){
        this.id = tesisFile.getId();
        this.name = tesisFile.getName();
        this.filename = tesisFile.getFilename();
        this.author = tesisFile.getAuthor();
        this.publicationDate = tesisFile.getPublicationDate();
        this.uploadDate = tesisFile.getUploadDate();
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
