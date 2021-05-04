package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.service.AdsorbentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/adsorbente")
public class AdsorbentController {

    @Autowired
    private AdsorbentService adsorbentService;

    @PostMapping(value= "")
    public AdsorbentResponse createAdsorbent(@RequestBody AdsorbentRequest request) {
        AdsorbentResponse response = null;
        try{
            validateAdsorbent(request);
            response = new AdsorbentResponse(adsorbentService.createAdsorbent(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Los adsorbentes deben tener un nombre", e);
        }
        return response;
    }

    @PutMapping(value= "")
    public AdsorbentResponse updateAdsorbent(@RequestBody AdsorbentRequest request) {
        AdsorbentResponse response = null;
        try{
            validateAdsorbentUpdate(request);
            response = new AdsorbentResponse(adsorbentService.updateAdsorbent(request));
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
    public void deleteAdsorbent(@PathVariable Long id){
        try {
            adsorbentService.deleteAdsorbent(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbente no existe", e);
        }
    }

    @GetMapping(value = "")
    public List<AdsorbentResponse> getAdsorbents(){
        List<AdsorbentResponse> adsorbents = new ArrayList<>();
        for (Adsorbent adsorbent : adsorbentService.getAll()) {
            adsorbents.add(new AdsorbentResponse(adsorbent));
        }
        return adsorbents;
    }

    private void validateAdsorbent(AdsorbentRequest request) throws InvalidRequestException {
        if(request.getNombre() == null || request.getNombre().isEmpty()) throw new InvalidRequestException();
    }
    private void validateAdsorbentUpdate(AdsorbentRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
    }
}
