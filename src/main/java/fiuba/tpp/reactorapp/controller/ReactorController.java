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
@RequestMapping("")
public class ReactorController {

    @Autowired
    private AdsorbatoService adsorbatoService;

    @Autowired
    private AdsorbenteService adsorbenteService;

    @Autowired
    private ReactorService reactorService;


    @PostMapping(value= "/adsorbato")
    public AdsorbatoResponse createAdsorbato(@RequestBody AdsorbatoRequest request) {
        AdsorbatoResponse response = null;
        try{
            validateAdsorbato(request);
            response = new AdsorbatoResponse(adsorbatoService.createAdsorbato(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Los adsorbatos deben tener un nombre", e);
        }
        return response;
    }

    @PutMapping(value= "/adsorbato")
    public AdsorbatoResponse updateAdsorbato(@RequestBody AdsorbatoRequest request) {
        AdsorbatoResponse response = null;
        try{
            validateAdsorbatoUpdate(request);
            response = new AdsorbatoResponse(adsorbatoService.updateAdsorbato(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del adsorbato", e);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbato no existe", e);
        }
        return response;
    }

    @DeleteMapping(value= "/adsorbato/{id}")
    public void deleteAdsorbato(@PathVariable Long id){
        try {
            adsorbatoService.deleteAdsorbato(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbato no existe", e);
        }

    }

    @GetMapping(value = "/adsorbato")
    public List<AdsorbatoResponse> getAdsorbatos(){
        List<AdsorbatoResponse> adsorbatos = new ArrayList<AdsorbatoResponse>();
        for (Adsorbato adsorbato: adsorbatoService.getAll()) {
            adsorbatos.add(new AdsorbatoResponse(adsorbato));
        }
        return adsorbatos;
    }

    @PostMapping(value= "/adsorbente")
    public AdsorbenteResponse createAdsorbente(@RequestBody AdsorbenteRequest request) {
        AdsorbenteResponse response = null;
        try{
            validateAdsorbente(request);
            response = new AdsorbenteResponse(adsorbenteService.createAdsorbente(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Los adsorbentes deben tener un nombre", e);
        }
        return response;
    }

    @PutMapping(value= "/adsorbente")
    public AdsorbenteResponse updateAdsorbente(@RequestBody AdsorbenteRequest request) {
        AdsorbenteResponse response = null;
        try{
            validateAdsorbenteUpdate(request);
            response = new AdsorbenteResponse(adsorbenteService.updateAdsorbente(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del adsorbente", e);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbente no existe", e);
        }
        return response;
    }

    @DeleteMapping(value= "/adsorbente/{id}")
    public void deleteAdsorbente(@PathVariable Long id){
        try {
            adsorbenteService.deleteAdsorbente(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbente no existe", e);
        }
    }

    @GetMapping(value = "/adsorbente")
    public List<AdsorbenteResponse> getAdsorbentes(){
        List<AdsorbenteResponse> adsorbentes = new ArrayList<AdsorbenteResponse>();
        for (Adsorbente adsorbente: adsorbenteService.getAll()) {
            adsorbentes.add(new AdsorbenteResponse(adsorbente));
        }
        return adsorbentes;
    }

    @PostMapping(value= "/reactor")
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

    @PutMapping(value= "/reactor")
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

    @DeleteMapping(value= "/reactor/{id}")
    public void deleteReactor(@PathVariable Long id){
        try {
            reactorService.deleteReactor(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El reactor no existe", e);
        }
    }

    @GetMapping(value = "/reactor")
    public List<ReactorResponse> getReactores(){
        List<ReactorResponse> reactores = new ArrayList<ReactorResponse>();
        for (Reactor reactor: reactorService.getAll()) {
            reactores.add(new ReactorResponse(reactor));
        }
        return reactores;
    }

    private void validateAdsorbato(AdsorbatoRequest request) throws InvalidRequestException {
        if(request.getNombreIon() == null || request.getNombreIon().isEmpty()) throw new InvalidRequestException();
    }

    private void validateAdsorbatoUpdate(AdsorbatoRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
    }

    private void validateAdsorbente(AdsorbenteRequest request) throws InvalidRequestException {
        if(request.getNombre() == null || request.getNombre().isEmpty()) throw new InvalidRequestException();
    }
    private void validateAdsorbenteUpdate(AdsorbenteRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
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
