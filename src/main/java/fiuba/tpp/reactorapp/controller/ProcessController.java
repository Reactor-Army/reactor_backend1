package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.response.ProcessResponse;
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
                    HttpStatus.BAD_REQUEST, "El proceso debe estar conformado de un adsorbato o un adsorbente", e);
        } catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Adsorbente o Adsorbato invalidos", e);
        }
        return response;
    }

    @PutMapping(value= "")
    public ProcessResponse updateProcess(@RequestBody ProcessRequest request) {
        ProcessResponse response = null;
        try{
            validateProcessUpdate(request);
            response = new ProcessResponse(processService.updateProcess(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del proceso", e);
        } catch (InvalidProcessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Adsorbato,Adsorbente o reactor invalido", e);
        }
        return response;
    }

    @DeleteMapping(value= "/{id}")
    public void deleteProcess(@PathVariable Long id){
        try {
            processService.deleteProcess(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El proceso no existe", e);
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
    public List<ProcessResponse> searchProcesses(@RequestParam(required = false) Long idAdsorbato, @RequestParam(required = false) Long idAdsorbente){
        List<ProcessResponse> processes = new ArrayList<>();
        ProcessFilter filter = new ProcessFilter(idAdsorbato,idAdsorbente);
        for (Process process : processService.search(filter)) {
            processes.add(new ProcessResponse(process));
        }
        return processes;
    }

    private void validateProcess(ProcessRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbato() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbente() == null ) throw new InvalidRequestException();
    }

    private void validateProcessUpdate(ProcessRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbato() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbente() == null ) throw new InvalidRequestException();
        if(request.getId()== null ) throw new InvalidRequestException();
    }

}
