package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.dto.SearchByAdsorbateDTO;
import fiuba.tpp.reactorapp.model.exception.*;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.ReactorVolumeRequest;
import fiuba.tpp.reactorapp.model.request.SearchByAdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.*;
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import fiuba.tpp.reactorapp.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/proceso")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private BreakCurvesService breakCurvesService;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value= "")
    public ProcessResponse createProcess(@RequestBody ProcessRequest request) {
        ProcessResponse response = null;
        try{
            validateProcess(request);
            response = new ProcessResponse(processService.createProcess(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.PROCESS_INVALID_REQUEST.getMessage(), e);
        } catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_PROCESS_CREATE.getMessage(), e);
        }catch (InvalidReactionOrderException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_REACTION_ORDER.getMessage(), e);
        }catch (InvalidKineticConstantException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_KINETIC_CONSTANT.getMessage(), e);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value= "/{id}")
    public ProcessResponse updateProcess(@PathVariable Long id, @RequestBody ProcessRequest request) {
        ProcessResponse response = null;
        try{
            validateKineticData(request.getReactionOrder(), request.getKineticConstant());
            response = new ProcessResponse(processService.updateProcess(id, request));
        } catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_PROCESS.getMessage(), e);
        }catch (InvalidReactionOrderException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_REACTION_ORDER.getMessage(), e);
        }catch (InvalidKineticConstantException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_KINETIC_CONSTANT.getMessage(), e);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value= "/{id}")
    public void deleteProcess(@PathVariable Long id){
        try {
            processService.deleteProcess(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping(value = "")
    public List<ProcessResponse> getProcesses(@RequestHeader(name ="Authorization", required = false) String authHeader,@RequestHeader(value = "User-Agent") String userAgent){
        List<ProcessResponse> processes = new ArrayList<>();
        for (Process process : processService.getProcesses(jwtUtils.isAnonymous(authHeader, userAgent))) {
            processes.add(new ProcessResponse(process));
        }
        return processes;
    }

    @GetMapping(value = "/buscar")
    public List<ProcessResponse> searchProcesses(@RequestParam(name="idAdsorbato",required = false) Long adsorbateId, @RequestParam(name="idAdsorbente", required = false) Long adsorbentId, @RequestHeader(name ="Authorization", required = false) String authHeader,@RequestHeader(value = "User-Agent") String userAgent){
        List<ProcessResponse> processes = new ArrayList<>();
        ProcessFilter filter = new ProcessFilter(adsorbateId,adsorbentId);
        for (Process process : processService.search(filter,jwtUtils.isAnonymous(authHeader, userAgent))){
            processes.add(new ProcessResponse(process));
        }
        return processes;
    }

    @PostMapping(value = "/adsorbato")
    public List<SearchByAdsorbateResponse> searchBestAdsorbentByAdsorbates(@RequestBody SearchByAdsorbateRequest request,@RequestHeader(name ="Authorization", required = false) String authHeader,@RequestHeader(value = "User-Agent") String userAgent){
        List<SearchByAdsorbateResponse> searchResults = new ArrayList<>();
        for (SearchByAdsorbateDTO result: processService.searchByAdsorbate(request,jwtUtils.isAnonymous(authHeader,userAgent))) {
            searchResults.add(new SearchByAdsorbateResponse(result,request.getAdsorbatesIds().size()));
        }
        return searchResults;
    }

    @GetMapping(value = "/{id}")
    public ProcessResponse getProcess(@PathVariable Long id,@RequestHeader(name ="Authorization", required = false) String authHeader, @RequestHeader(value = "User-Agent") String userAgent) {
        try {
            return new ProcessResponse(processService.getById(id,jwtUtils.isAnonymous(authHeader, userAgent)));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e);
        }
    }

    @PostMapping(value = "/{id}/reactor/volumen")
    public ReactorVolumeResponse calculateReactorVolume(@PathVariable Long id, @RequestBody ReactorVolumeRequest request) {
        try {
            validateVolumeRequest(request);
            return processService.calculateVolume(id,request);

        }catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_VOLUME_REQUEST.getMessage(), e);
        }catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e);
        }catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_KINECT_INFORMATION.getMessage(), e);
        }
    }

    @GetMapping("/{id}/curvas")
    public List<BreakCurvesDataResponse> getBreakCurvesDataByProcess(@PathVariable Long id){
        try {
            return breakCurvesService.getBreakCurvesDataByProcess(id);
        }catch(InvalidRequestException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    private void validateProcess(ProcessRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbate() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbent() == null ) throw new InvalidRequestException();
        validateKineticData(request.getReactionOrder(),request.getKineticConstant());
    }

    private void validateKineticData(Integer reactionOrder, Float kineticConstant){
        if(reactionOrder != null && reactionOrder != 1 && reactionOrder != 2) throw new InvalidReactionOrderException();
        if(kineticConstant != null && kineticConstant <= 0) throw new InvalidKineticConstantException();
    }

    private void validateVolumeRequest(ReactorVolumeRequest request){
        if(request.getInitialConcentration() == null || request.getInitialConcentration() <= 0
                || request.getFinalConcentration() == null || request.getFinalConcentration() <= 0
                || request.getFlow() == null || request.getFlow() <= 0) throw new InvalidRequestException();

    }
}
