package fiuba.tpp.reactorapp.model.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class TesisFileRequest {

    private String name;

    private String author;

    private MultipartFile tesis;

    private Date fechaPublicacion;

    private String procesosIds;


    public TesisFileRequest(String name, String author, MultipartFile tesis, Date fechaPublicacion, String procesosIds) {
        this.name = name;
        this.author = author;
        this.tesis = tesis;
        this.fechaPublicacion = fechaPublicacion;
        this.procesosIds = procesosIds;
    }

    public MultipartFile getTesis() {
        return tesis;
    }

    public void setTesis(MultipartFile tesis) {
        this.tesis = tesis;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getProcesosIds() {
        return procesosIds;
    }

    public void setProcesosIds(String procesosIds) {
        this.procesosIds = procesosIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
