package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbatoResponse;
import fiuba.tpp.reactorapp.service.AdsorbatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/adsorbato")
public class AdsorbatoController {

    @Autowired
    private AdsorbatoService adsorbatoService;

    @PostMapping(value= "")
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

    @PutMapping(value= "")
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

    @DeleteMapping(value= "/{id}")
    public void deleteAdsorbato(@PathVariable Long id){
        try {
            adsorbatoService.deleteAdsorbato(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El adsorbato no existe", e);
        }

    }

    @GetMapping(value = "")
    public List<AdsorbatoResponse> getAdsorbatos(){
        List<AdsorbatoResponse> adsorbatos = new ArrayList<>();
        for (Adsorbato adsorbato: adsorbatoService.getAll()) {
            adsorbatos.add(new AdsorbatoResponse(adsorbato));
        }
        return adsorbatos;
    }

    @GetMapping(value = "/search")
    public List<AdsorbatoResponse> searchAdsorbatos(@RequestParam(required = false) String nombre, @RequestParam(required = false) Integer cargaIon){
        List<AdsorbatoResponse> adsorbatos = new ArrayList<>();
        AdsorbatoFilter filter = new AdsorbatoFilter(nombre,cargaIon);
        for (Adsorbato adsorbato: adsorbatoService.search(filter)) {
            adsorbatos.add(new AdsorbatoResponse(adsorbato));
        }
        return adsorbatos;
    }



    private void validateAdsorbato(AdsorbatoRequest request) throws InvalidRequestException {
        if(request.getNombreIon() == null || request.getNombreIon().isEmpty()) throw new InvalidRequestException();
    }

    private void validateAdsorbatoUpdate(AdsorbatoRequest request) throws InvalidRequestException {
        if(request.getId() == null) throw new InvalidRequestException();
    }
}
