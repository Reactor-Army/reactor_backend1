package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.exception.*;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/curvas-ruptura")
public class BreakCurvesController {

    @Autowired
    private BreakCurvesService breakCurvesService;

    private static final String FILE_EXTENSION = "csv";

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

    private void validateThomasRequest(ThomasRequest request, Errors errors){
        handleErrors(errors);
        if(request.getCaudalVolumetrico() == null || request.getCaudalVolumetrico() == 0) throw new InvalidRequestException();
        if(request.getConcentracionInicial() == null || request.getConcentracionInicial() == 0) throw new InvalidRequestException();
        if(request.getSorbenteReactor() == null || request.getSorbenteReactor() == 0) throw  new InvalidRequestException();
        validateFile(request.getObservaciones());
    }
    private void validateFile(MultipartFile file) {
        if(file == null ||file.isEmpty()) throw new FileNotFoundException();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(!extension.equalsIgnoreCase(FILE_EXTENSION)) throw new InvalidFileException();
    }

    private void handleErrors(Errors errors){
        if (errors.hasFieldErrors()) {
            throw new InvalidFieldException();
        }
    }
}
