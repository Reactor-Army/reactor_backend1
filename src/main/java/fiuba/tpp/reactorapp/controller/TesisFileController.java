package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.controller.utils.FileUtils;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.FileNotFoundException;
import fiuba.tpp.reactorapp.model.exception.FileSizeExceedException;
import fiuba.tpp.reactorapp.model.exception.InvalidFileException;
import fiuba.tpp.reactorapp.model.exception.TesisNotFoundException;
import fiuba.tpp.reactorapp.model.request.TesisFileRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.TesisFileResponse;
import fiuba.tpp.reactorapp.service.TesisFileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tesis-archivos")
public class TesisFileController {


    //50 MB
    private static final Long MAX_FILE_SIZE = 50000000L;

    @Autowired
    private TesisFileService tesisFileService;

    @PostMapping("/subir")
    public TesisFileResponse uploadTesisFile(@ModelAttribute TesisFileRequest request){
        try{
            validateTesisFile(request);
            return tesisFileService.uploadFile(request);
        } catch (FileSizeExceedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.TESIS_FILE_SIZE_EXCEED.getMessage(), e);
        }catch (FileNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch (InvalidFileException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_TESIS_FILE.getMessage(), e);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }

    }

    @PutMapping("/{id}")
    public TesisFileResponse changeTesisFile(@PathVariable Long id,@ModelAttribute TesisFileRequest request){
        try{
            validateTesisFile(request);
            return tesisFileService.changeFile(id,request);
        } catch (FileSizeExceedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.TESIS_FILE_SIZE_EXCEED.getMessage(), e);
        }catch (FileNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch (InvalidFileException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_TESIS_FILE.getMessage(), e);
        }catch (TesisNotFoundException e) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ResponseMessage.TESIS_NOT_FOUND.getMessage(), e);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }

    }

    @DeleteMapping("/{id}")
    public void deleteTesisFile(@PathVariable Long id){
        try{
            tesisFileService.deleteFile(id);
        }catch (TesisNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.TESIS_NOT_FOUND.getMessage(), e);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<ByteArrayResource> downloadTesis(@PathVariable Long id){
        FileTemplateDTO dto;
        try{
            dto = tesisFileService.downloadFile(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.TESIS_NOT_FOUND.getMessage(), e);
        }
        return FileUtils.generateFileResponse(dto);
    }

    private void validateTesisFile(TesisFileRequest request) {
        validateFile(request.getTesis());
    }


    private void validateFile(MultipartFile file) {
        if(file == null ||file.isEmpty()) throw new FileNotFoundException();
        validateTesisExtensions(Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename())));
        if(file.getSize() > MAX_FILE_SIZE) throw new FileSizeExceedException();
    }

    private void validateTesisExtensions(String extension){
        List<String> validExtensions = Arrays.asList("pdf", "doc", "docx");
        boolean invalidExtension = !validExtensions.contains(extension.toLowerCase());
        if(invalidExtension){
            throw new InvalidFileException();
        }
    }
}
