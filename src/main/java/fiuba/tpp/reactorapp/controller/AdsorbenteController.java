package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbenteResponse;
import fiuba.tpp.reactorapp.service.AdsorbenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/adsorbente")
public class AdsorbenteController {

    @Autowired
    private AdsorbenteService adsorbenteService;

    @PostMapping(value= "")
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

    @PutMapping(value= "")
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

    @DeleteMapping(value= "/{id}")
    public void deleteAdsorbente(@PathVariable Long id){
        try {
            adsorbenteService.deleteAdsorbente(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbente no existe", e);
        }
    }

    @GetMapping(value = "")
    public List<AdsorbenteResponse> getAdsorbentes(){
        List<AdsorbenteResponse> adsorbentes = new ArrayList<>();
        for (Adsorbente adsorbente: adsorbenteService.getAll()) {
            adsorbentes.add(new AdsorbenteResponse(adsorbente));
        }
        return adsorbentes;
    }

    private void validateAdsorbente(AdsorbenteRequest request) throws InvalidRequestException {
        if(request.getNombre() == null || request.getNombre().isEmpty()) throw new InvalidRequestException();
    }
    private void validateAdsorbenteUpdate(AdsorbenteRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
    }
}
