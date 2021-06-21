package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.dto.SearchByAdsorbateDTO;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.SearchByAdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.ProcessResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.SearchByAdsorbateResponse;
import fiuba.tpp.reactorapp.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        } catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PutMapping(value= "/{id}")
    public ProcessResponse updateProcess(@PathVariable Long id, @RequestBody ProcessRequest request) {
        ProcessResponse response = null;
        try{
            response = new ProcessResponse(processService.updateProcess(id, request));
        } catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_PROCESS.getMessage(), e);
        } catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

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
    public List<ProcessResponse> getProcesses(){
        List<ProcessResponse> processes = new ArrayList<>();
        for (Process process : processService.getAll()) {
            processes.add(new ProcessResponse(process));
        }
        return processes;
    }

    @GetMapping(value = "/buscar")
    public List<ProcessResponse> searchProcesses(@RequestParam(name="idAdsorbato",required = false) Long adsorbateId, @RequestParam(name="idAdsorbente", required = false) Long adsorbentId){
        List<ProcessResponse> processes = new ArrayList<>();
        ProcessFilter filter = new ProcessFilter(adsorbateId,adsorbentId);
        for (Process process : processService.search(filter)) {
            processes.add(new ProcessResponse(process));
        }
        return processes;
    }

    @PostMapping(value = "/adsorbato")
    public List<SearchByAdsorbateResponse> searchBestAdsorbentByAdsorbates(@RequestBody SearchByAdsorbateRequest request){
        List<SearchByAdsorbateResponse> searchResults = new ArrayList<>();
        for (SearchByAdsorbateDTO result: processService.searchByAdsorbate(request)) {
            searchResults.add(new SearchByAdsorbateResponse(result,request.getAdsorbatesIds().size()));
        }
        return searchResults;
    }

    @GetMapping(value = "/{id}")
    public ProcessResponse getProcess(@PathVariable Long id) {
        try {
            return new ProcessResponse(processService.getById(id));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e);
        }
    }
    private void validateProcess(ProcessRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbate() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbent() == null ) throw new InvalidRequestException();
    }
}
