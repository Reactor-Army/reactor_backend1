package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbatoResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbenteResponse;
import fiuba.tpp.reactorapp.model.response.ErrorResponse;
import fiuba.tpp.reactorapp.model.response.ReactorResponse;
import fiuba.tpp.reactorapp.service.AdsorbatoService;
import fiuba.tpp.reactorapp.service.AdsorbenteService;
import fiuba.tpp.reactorapp.service.ReactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/reactor")
public class ReactorController {

    @Autowired
    private ReactorService reactorService;

    @PostMapping(value= "")
    public ReactorResponse createReactor(@RequestBody ReactorRequest request) {
        ReactorResponse response = null;
        try{
            validateReactor(request);
            response = new ReactorResponse(reactorService.createReactor(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El reactor debe estar conformado de un adsorbato o un adsorbente", e);
        } catch (InvalidReactorException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Adsorbente o Adsorbato invalidos", e);
        }
        return response;
    }

    @PutMapping(value= "")
    public ReactorResponse updateReactor(@RequestBody ReactorRequest request) {
        ReactorResponse response = null;
        try{
            validateReactorUpdate(request);
            response = new ReactorResponse(reactorService.updateReactor(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del reactor", e);
        } catch (InvalidReactorException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Adsorbato,Adsorbente o reactor invalido", e);
        }
        return response;
    }

    @DeleteMapping(value= "/{id}")
    public void deleteReactor(@PathVariable Long id){
        try {
            reactorService.deleteReactor(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El reactor no existe", e);
        }
    }

    @GetMapping(value = "")
    public List<ReactorResponse> getReactores(){
        List<ReactorResponse> reactores = new ArrayList<ReactorResponse>();
        for (Reactor reactor: reactorService.getAll()) {
            reactores.add(new ReactorResponse(reactor));
        }
        return reactores;
    }

    private void validateReactor(ReactorRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbato() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbente() == null ) throw new InvalidRequestException();
    }

    private void validateReactorUpdate(ReactorRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbato() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbente() == null ) throw new InvalidRequestException();
        if(request.getId()== null ) throw new InvalidRequestException();
    }

}
