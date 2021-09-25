package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.*;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/curvas-ruptura")
public class BreakCurvesController {

    @Autowired
    private BreakCurvesService breakCurvesService;

    @PostMapping(value= "/thomas")
    public ThomasResponse thomas(@ModelAttribute ThomasRequest request, Errors errors){
        try{
            validateThomasRequest(request, errors);
            return breakCurvesService.calculateByThomas(request);
        }catch(FileNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_THOMAS.getMessage(), e);
        }catch(InvalidFileException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FILE.getMessage(), e);
        }catch(InvalidCSVFormatException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_HEADER.getMessage(), e);
        }catch(InvalidFieldException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FIELDS.getMessage(), e);
        }
    }

    @PostMapping(value= "/yoon-nelson")
    public YoonNelsonResponse yoonNelson(@ModelAttribute YoonNelsonRequest request, Errors errors){
        try{
            validateYoonNelsonRequest(request,errors);
            return breakCurvesService.calculateByYoonNelson(request);
        }catch(FileNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_NELSON.getMessage(), e);
        }catch(InvalidFileException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FILE.getMessage(), e);
        }catch(InvalidCSVFormatException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_HEADER.getMessage(), e);
        }catch(InvalidFieldException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FIELDS.getMessage(), e);
        }

    }

    @PostMapping(value= "/adams-bohart")
    public AdamsBohartResponse adamsBohart(@ModelAttribute AdamsBohartRequest request, Errors errors){
        try{
            validateAdamsBohart(request,errors);
            return breakCurvesService.calculateByAdamsBohart(request);
        }catch(FileNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_BOHART.getMessage(), e);
        }catch(InvalidFileException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FILE.getMessage(), e);
        }catch(InvalidCSVFormatException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_HEADER.getMessage(), e);
        }catch(InvalidFieldException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FIELDS.getMessage(), e);
        }

    }

    @GetMapping(value = "/ejemplo")
    public ResponseEntity<ByteArrayResource> downloadDataTemplate() {
        FileTemplateDTO dto;
        try{
            dto = breakCurvesService.getDataTemplateFile();
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return generateFileResponse(dto);
    }

    private ResponseEntity<ByteArrayResource> generateFileResponse(FileTemplateDTO dto){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ dto.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(dto.getFileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getResource());
    }

    private void validateThomasRequest(ThomasRequest request, Errors errors){
        handleErrors(errors);
        if(request.getCaudalVolumetrico() == null || request.getCaudalVolumetrico() == 0) throw new InvalidRequestException();
        if(request.getConcentracionInicial() == null || request.getConcentracionInicial() == 0) throw new InvalidRequestException();
        if(request.getSorbenteReactor() == null || request.getSorbenteReactor() == 0) throw  new InvalidRequestException();
        validateFile(request.getObservaciones());
    }

    private void validateYoonNelsonRequest(YoonNelsonRequest request, Errors errors){
        handleErrors(errors);
        if(request.getCaudalVolumetrico() == null || request.getCaudalVolumetrico() == 0) throw new InvalidRequestException();
        validateFile(request.getObservaciones());
    }

    private void validateAdamsBohart(AdamsBohartRequest request, Errors errors){
        handleErrors(errors);
        if(request.getAlturaLechoReactor()== null || request.getAlturaLechoReactor() == 0) throw new InvalidRequestException();
        if(request.getVelocidadLineal() == null || request.getVelocidadLineal() == 0) throw new InvalidRequestException();
        if(request.getCaudalVolumetrico() == null || request.getCaudalVolumetrico() == 0) throw new InvalidRequestException();
        if(request.getConcentracionInicial() == null || request.getConcentracionInicial() == 0) throw new InvalidRequestException();
        validateFile(request.getObservaciones());
    }


    private void validateFile(MultipartFile file) {
        if(file == null ||file.isEmpty()) throw new FileNotFoundException();
        validateExtensions(Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename())));
    }

    private void validateExtensions(String extension){
        List<String> validExtensions = Arrays.asList("csv", "xls", "xlsx");
        boolean invalidExtension = !validExtensions.contains(extension.toLowerCase());
        if(invalidExtension){
            throw new InvalidFileException();
        }
    }

    private void handleErrors(Errors errors){
        if (errors.hasFieldErrors()) {
            throw new InvalidFieldException();
        }
    }
}
