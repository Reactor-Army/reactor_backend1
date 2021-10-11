package fiuba.tpp.reactorapp.controller.utils;

import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileUtils {

    private FileUtils(){
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<ByteArrayResource> generateFileResponse(FileTemplateDTO dto){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ dto.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(dto.getFileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }
}
