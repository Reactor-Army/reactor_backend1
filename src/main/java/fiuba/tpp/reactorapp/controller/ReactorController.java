package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbatoResponse;
import fiuba.tpp.reactorapp.model.response.ErrorResponse;
import fiuba.tpp.reactorapp.service.AdsorbatoService;
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

    private void validateAdsorbato(AdsorbatoRequest request) throws InvalidRequestException {
        if(request.getNombreIon() == null || request.getNombreIon().isEmpty()) throw new InvalidRequestException();
    }

}
