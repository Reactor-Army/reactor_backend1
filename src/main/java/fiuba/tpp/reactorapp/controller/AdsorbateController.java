package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.service.AdsorbateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/adsorbato")
public class AdsorbateController {

    @Autowired
    private AdsorbateService adsorbateService;

    @PostMapping(value= "")
    public AdsorbateResponse createAdsorbate(@RequestBody AdsorbateRequest request) {
        AdsorbateResponse response = null;
        try{
            validateAdsorbate(request);
            response = new AdsorbateResponse(adsorbateService.createAdsorbate(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Los adsorbatos deben tener un nombre", e);
        }
        return response;
    }

    @PutMapping(value= "")
    public AdsorbateResponse updateAdsorbate(@RequestBody AdsorbateRequest request) {
        AdsorbateResponse response = null;
        try{
            validateAdsorbateUpdate(request);
            response = new AdsorbateResponse(adsorbateService.updateAdsorbate(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Es necesario el ID del adsorbato", e);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbato no existe", e);
        }
        return response;
    }

    @DeleteMapping(value= "/{id}")
    public void deleteAdsorbate(@PathVariable Long id){
        try {
            adsorbateService.deleteAdsorbate(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbato no existe", e);
        }

    }

    @GetMapping(value = "")
    public List<AdsorbateResponse> getAdsorbates(){
        List<AdsorbateResponse> adsorbates = new ArrayList<>();
        for (Adsorbate adsorbate : adsorbateService.getAll()) {
            adsorbates.add(new AdsorbateResponse(adsorbate));
        }
        return adsorbates;
    }

    @GetMapping(value = "/buscar")
    public List<AdsorbateResponse> searchAdsorbates(@RequestParam(name="nombre",required = false) String name, @RequestParam(name="cargaIon",required = false) Integer ionCharge){
        List<AdsorbateResponse> adsorbates = new ArrayList<>();
        AdsorbateFilter filter = new AdsorbateFilter(name, ionCharge);
        for (Adsorbate adsorbate : adsorbateService.search(filter)) {
            adsorbates.add(new AdsorbateResponse(adsorbate));
        }
        return adsorbates;
    }



    private void validateAdsorbate(AdsorbateRequest request) throws InvalidRequestException {
        if(request.getNombreIon() == null || request.getNombreIon().isEmpty()) throw new InvalidRequestException();
    }

    private void validateAdsorbateUpdate(AdsorbateRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
    }
}
