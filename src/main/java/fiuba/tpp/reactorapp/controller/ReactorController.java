package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class ReactorController {

    @Autowired
    private AdsorbatoService adsorbatoService;

    @Autowired
    private AdsorbenteService adsorbenteService;

    @Autowired
    private ReactorService reactorService;


    @PostMapping(value= "/adsorbato")
    public ResponseEntity<?> createAdsorbato(@RequestBody AdsorbatoRequest request) {
        AdsorbatoResponse response = null;
        try{
            validateAdsorbato(request);
            response = new AdsorbatoResponse(adsorbatoService.createAdsorbato(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Los adsorbatos deben tener un nombre"));

        }
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> createAdsorbente(@RequestBody AdsorbenteRequest request) {
        AdsorbenteResponse response = null;
        try{
            validateAdsorbente(request);
            response = new AdsorbenteResponse(adsorbenteService.createAdsorbente(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Los adsorbentes deben tener un nombre"));
        }
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> createReactor(@RequestBody ReactorRequest request) {
        ReactorResponse response = null;
        try{
            validateReactor(request);
            response = new ReactorResponse(reactorService.createReactor(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Los adsorbentes deben tener un nombre"));
        } catch (InvalidReactorException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Adsorbato o Adsorbente invalidos"));
        }
        return ResponseEntity.ok(response);
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

    private void validateAdsorbente(AdsorbenteRequest request) throws InvalidRequestException {
        if(request.getNombre() == null || request.getNombre().isEmpty()) throw new InvalidRequestException();
    }

    private void validateReactor(ReactorRequest request) throws InvalidRequestException {
        if(request.getIdAdsorbato() == null ) throw new InvalidRequestException();
        if(request.getIdAdsorbente() == null ) throw new InvalidRequestException();
    }

}
