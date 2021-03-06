package fiuba.tpp.reactorapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fiuba.tpp.reactorapp.controller.utils.FileUtils;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.*;
import fiuba.tpp.reactorapp.model.request.BreakCurveDataRequest;
import fiuba.tpp.reactorapp.model.request.ReactorQRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.BreakCurvesDataResponse;
import fiuba.tpp.reactorapp.model.response.ReactorQResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

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
    public ThomasResponse thomas(@ModelAttribute ThomasRequest request, @ApiIgnore Errors errors){
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
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PostMapping(value= "/yoon-nelson")
    public YoonNelsonResponse yoonNelson(@ModelAttribute YoonNelsonRequest request, @ApiIgnore Errors errors){
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
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }

    }

    @PostMapping(value= "/adams-bohart")
    public AdamsBohartResponse adamsBohart(@ModelAttribute AdamsBohartRequest request, @ApiIgnore Errors errors){
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
        }catch(InvalidFieldException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_FIELDS.getMessage(), e);
        } catch(Exception e){
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping(value = "/ejemplo")
    public ResponseEntity<ByteArrayResource> downloadDataTemplate() {
        FileTemplateDTO dto;
        try{
            dto = breakCurvesService.getDataTemplateFile();
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return FileUtils.generateFileResponse(dto);
    }

    @GetMapping("/{id}")
    public BreakCurvesDataResponse getBreakCurveData(@PathVariable Long id){
        try{
            return breakCurvesService.getBreakCurveData(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.DATA_NOT_FOUND.getMessage(), e);
        }
    }

    @PostMapping("/{id}")
    public BreakCurvesDataResponse saveBreakCurveData(@PathVariable Long id, @RequestBody BreakCurveDataRequest request){
        try{
            validateBreakCurvesDataRequest(request);
            return breakCurvesService.saveBreakCurveData(id,request);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_BREAK_CURVE_DATA.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBreakCurveData(@PathVariable Long id){
        try{
            breakCurvesService.deleteBreakCurveData(id);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.DATA_NOT_FOUND.getMessage(), e);
        }
    }

    @GetMapping(value= "/thomas")
    public BreakCurvesDataResponse getFreeThomas(){
        try{
            return breakCurvesService.getFreeData(EModel.THOMAS);
        }catch (ComponentNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.DATA_NOT_FOUND.getMessage(), e);
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping(value= "/yoon-nelson")
    public BreakCurvesDataResponse getFreeYoonNelson(){
        try{
            return breakCurvesService.getFreeData(EModel.YOON_NELSON);
        }catch (ComponentNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.DATA_NOT_FOUND.getMessage(), e);
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping(value= "/adams-bohart")
    public BreakCurvesDataResponse getFreeAdams(){
        try{
            return breakCurvesService.getFreeData(EModel.ADAMS_BOHART);
        }catch (ComponentNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.DATA_NOT_FOUND.getMessage(), e);
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PostMapping("/reactor-q")
    public ReactorQResponse calculateReactorQ(@RequestBody ReactorQRequest request){
        try{
            validateReactorQRequest(request);
            return breakCurvesService.calculateQValue(request.getCurveId(), request.getBaselineId());
        }catch(InvalidRequestException | ComponentNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_REACTOR_Q_REQUEST.getMessage(), e);
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    private void validateReactorQRequest(ReactorQRequest request){
        if(request.getCurveId() == null) throw new InvalidRequestException();
        if(request.getBaselineId() == null) throw new InvalidRequestException();
    }

    private void validateBreakCurvesDataRequest(BreakCurveDataRequest request){
        if(request.getName() == null || request.getName().isEmpty()) throw new InvalidRequestException();
        if(request.getProcessId() == null || request.getProcessId() == 0) throw new InvalidRequestException();
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
