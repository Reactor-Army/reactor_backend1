package fiuba.tpp.reactorapp.entities;

import fiuba.tpp.reactorapp.model.request.TesisFileRequest;
import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name ="TESIS_FILE")
public class TesisFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private Date publicationDate;

    private Date uploadDate;

    @OneToMany
    private List<Process> processes;

    @Lob
    private byte[] data;

    public TesisFile() {
        //Necesario para JPA
    }

    public TesisFile(TesisFileRequest request) throws IOException {
        coyData(request);
    }

    private void coyData(TesisFileRequest request) throws IOException {
        this.name = FilenameUtils.getBaseName(request.getTesis().getOriginalFilename());
        this.data = request.getTesis().getBytes();
        this.type = FilenameUtils.getExtension(request.getTesis().getOriginalFilename());
        this.publicationDate = request.getFechaPublicacion();
        this.uploadDate = Calendar.getInstance().getTime();
    }

    public TesisFile update(TesisFileRequest request) throws IOException {
        coyData(request);
        return this;
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
