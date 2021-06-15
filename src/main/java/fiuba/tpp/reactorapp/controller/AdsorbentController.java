package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateAdsorbentException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentNameResponse;
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
        } catch (DuplicateAdsorbentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un adsorbente con ese nombre y ese tamaño de particula", e);
        }
        return response;
    }

    @PutMapping(value= "/{id}")
    public AdsorbentResponse updateAdsorbent(@PathVariable Long id, @RequestBody AdsorbentRequest request) {
        AdsorbentResponse response = null;
        try{
            validateAdsorbentUpdate(id);
            response = new AdsorbentResponse(adsorbentService.updateAdsorbent(id, request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del adsorbente", e);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbente no existe", e);
        }catch (DuplicateAdsorbentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe un adsorbente con ese nombre y ese tamaño de particula", e);
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

    @GetMapping(value = "/buscar")
    public List<AdsorbentResponse> searchAdsorbents(@RequestParam(name="nombre",required = false) String name){
        List<AdsorbentResponse> adsorbents = new ArrayList<>();
        AdsorbentFilter filter = new AdsorbentFilter(name);
        for (Adsorbent adsorbent : adsorbentService.search(filter)) {
            adsorbents.add(new AdsorbentResponse(adsorbent));
        }
        return adsorbents;
    }

    @GetMapping(value = "/buscar/nombre")
    public List<AdsorbentNameResponse> searchAdsorbentsName(@RequestParam(name="nombre",required = false) String name){
        List<AdsorbentNameResponse> adsorbentsNames = new ArrayList<>();
        AdsorbentFilter filter = new AdsorbentFilter(name);
        for (Adsorbent adsorbent : adsorbentService.search(filter)) {
            adsorbentsNames.add(new AdsorbentNameResponse(adsorbent));
        }
        return adsorbentsNames;
    }

    @GetMapping(value = "/{id}")
    public AdsorbentResponse getAdsorbent(@PathVariable Long id) {
        try {
            return new AdsorbentResponse(adsorbentService.getById(id));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El Adsorbente no existe", e);
        }
    }


    private void validateAdsorbent(AdsorbentRequest request) throws InvalidRequestException {
        if(request.getName() == null || request.getName().isEmpty()) throw new InvalidRequestException();
    }
    private void validateAdsorbentUpdate(Long id) throws InvalidRequestException {
        if(id == null) throw new InvalidRequestException();
    }
}
