package fiuba.tpp.reactorapp.model.dto;

import org.springframework.core.io.ByteArrayResource;

public class FileTemplateDTO {

    private ByteArrayResource resource;

    private String fileName;

    private long fileSize;

    public FileTemplateDTO(ByteArrayResource resource, String fileName, long fileSize) {
        this.resource = resource;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public ByteArrayResource getResource() {
        return resource;
    }

    public void setResource(ByteArrayResource resource) {
        this.resource = resource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
