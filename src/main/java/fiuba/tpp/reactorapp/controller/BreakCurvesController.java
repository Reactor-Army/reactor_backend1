package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.exception.FileNotFoundException;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/curvasRuptura")
public class BreakCurvesController {

    @Autowired
    private BreakCurvesService breakCurvesService;

    @PostMapping(value= "/thomas",consumes = {"multipart/form-data"})
    public RegressionResult thomas(@ModelAttribute ThomasRequest request){
        try{
            validateFile(request.getObservations());
            return breakCurvesService.calculateByThomas(request);
        }catch(FileNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.FILE_NOT_FOUND.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if(file == null ||file.isEmpty()) throw new FileNotFoundException();
    }
}
