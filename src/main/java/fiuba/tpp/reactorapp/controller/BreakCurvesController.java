package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.exception.FileNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/curvas-ruptura")
public class BreakCurvesController {

    @Autowired
    private BreakCurvesService breakCurvesService;

    @PostMapping(value= "/thomas",consumes = {"multipart/form-data"})
    public ThomasResponse thomas(@ModelAttribute ThomasRequest request){
        try{
            validateThomasRequest(request);
            return breakCurvesService.calculateByThomas(request);
        }catch(FileNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_THOMAS.getMessage(), e);
        }
    }

    private void validateThomasRequest(ThomasRequest request){
        if(request.getCaudalVolumetrico() == null || request.getCaudalVolumetrico() == 0) throw new InvalidRequestException();
        if(request.getConcentracionInicial() == null || request.getConcentracionInicial() == 0) throw new InvalidRequestException();
        if(request.getSorbenteReactor() == null || request.getSorbenteReactor() == 0) throw  new InvalidRequestException();
        validateFile(request.getObservaciones());
    }
    private void validateFile(MultipartFile file) {
        if(file == null ||file.isEmpty()) throw new FileNotFoundException();
    }
}
