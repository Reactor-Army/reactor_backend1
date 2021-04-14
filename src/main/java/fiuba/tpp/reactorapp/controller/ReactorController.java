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

    @PutMapping(value= "/adsorbato")
    public ResponseEntity<?> updateAdsorbato(@RequestBody AdsorbatoRequest request) {
        AdsorbatoResponse response = null;
        try{
            validateAdsorbatoUpdate(request);
            response = new AdsorbatoResponse(adsorbatoService.updateAdsorbato(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Es necesario el ID del adsorbato"));

        } catch (ComponentNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("El adsorbato no existe"));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value= "/adsorbato/{id}")
    public ResponseEntity<?> deleteAdsorbato(@PathVariable Long id){
        try {
            adsorbatoService.deleteAdsorbato(id);
            return ResponseEntity.ok().body(new ErrorResponse("Adsorbato borrado correctamente"));
        } catch (ComponentNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("El adsorbato no existe"));
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

    @PutMapping(value= "/adsorbente")
    public ResponseEntity<?> updateAdsorbente(@RequestBody AdsorbenteRequest request) {
        AdsorbenteResponse response = null;
        try{
            validateAdsorbenteUpdate(request);
            response = new AdsorbenteResponse(adsorbenteService.updateAdsorbente(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Es necesario el ID del adsorbente"));

        } catch (ComponentNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("El adsorbente no existe"));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value= "/adsorbente/{id}")
    public ResponseEntity<?> deleteAdsorbente(@PathVariable Long id){
        try {
            adsorbenteService.deleteAdsorbente(id);
            return ResponseEntity.ok().body(new ErrorResponse("Adsorbente borrado correctamente"));
        } catch (ComponentNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("El adsorbente no existe"));
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

    @PutMapping(value= "/reactor")
    public ResponseEntity<?> updateReactor(@RequestBody ReactorRequest request) {
        ReactorResponse response = null;
        try{
            validateReactorUpdate(request);
            response = new ReactorResponse(reactorService.updateReactor(request));
        } catch (InvalidRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Es necesario el ID del reactpr"));
        } catch (InvalidReactorException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Adsorbato,Adsorbente o reactor invalido"));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value= "/reactor/{id}")
    public ResponseEntity<?> deleteReactor(@PathVariable Long id){
        try {
            reactorService.deleteReactor(id);
            return ResponseEntity.ok().body(new ErrorResponse("Reactor borrado correctamente"));
        } catch (ComponentNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("El reactor no existe"));
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
