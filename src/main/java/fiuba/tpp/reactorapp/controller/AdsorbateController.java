package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateIUPACNameException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateNameResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.model.response.ProcessCountResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
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
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_ADSORBATE.getMessage(), e);
        } catch (DuplicateIUPACNameException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_ADSORBATE.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PutMapping(value= "/{id}")
    public AdsorbateResponse updateAdsorbate(@PathVariable Long id, @RequestBody AdsorbateRequest request) {
        AdsorbateResponse response = null;
        try{
            validateAdsorbateUpdate(request);
            response = new AdsorbateResponse(adsorbateService.updateAdsorbate(id, request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.ADSORBATE_INVALID_REQUEST.getMessage(), e);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.ADSORBATE_NOT_FOUND.getMessage(), e);
        } catch (DuplicateIUPACNameException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_ADSORBATE.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @DeleteMapping(value= "/{id}")
    public void deleteAdsorbate(@PathVariable Long id){
        try {
            adsorbateService.deleteAdsorbate(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.ADSORBATE_NOT_FOUND.getMessage(), e);
        } catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
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

    @GetMapping(value = "/buscar/nombre")
    public List<AdsorbateNameResponse> searchAdsorbatesName(@RequestParam(name="nombre",required = false) String name){
        List<AdsorbateNameResponse> adsorbatesName = new ArrayList<>();
        AdsorbateFilter filter = new AdsorbateFilter(name);
        for (Adsorbate adsorbate : adsorbateService.search(filter)) {
            adsorbatesName.add(new AdsorbateNameResponse(adsorbate));
        }
        return adsorbatesName;
    }

    @GetMapping(value = "/{id}")
    public AdsorbateResponse getAdsorbate(@PathVariable Long id) {
        try {
            return new AdsorbateResponse((adsorbateService.getById(id)));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.ADSORBATE_NOT_FOUND.getMessage(), e);
        }
    }

    @GetMapping(value = "/{id}/cantidad-procesos")
    public ProcessCountResponse getAdsorbateProcessCount(@PathVariable Long id) {
        return new ProcessCountResponse(adsorbateService.getAdsorbateProcessCount(id));
    }

    private void validateAdsorbate(AdsorbateRequest request) throws InvalidRequestException {
        if(request.getIonName() == null || request.getIonName().isEmpty()) throw new InvalidRequestException();
        if(request.getNameIUPAC() == null || request.getNameIUPAC().isEmpty()) throw new InvalidRequestException();
    }

    private void validateAdsorbateUpdate(AdsorbateRequest request) throws InvalidRequestException {
        if(request.getNameIUPAC() == null || request.getNameIUPAC().isEmpty()) throw new InvalidRequestException();
    }
}
