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
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.service.AdsorbateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public List<AdsorbateResponse> getAdsorbates(@RequestHeader(name ="Authorization", required = false) String authHeader, @RequestHeader(value = "User-Agent") String userAgent){
        List<AdsorbateResponse> response = new ArrayList<>();
        for (Adsorbate adsorbate: adsorbateService.getAdsorbates(jwtUtils.isAnonymous(authHeader, userAgent))) {
            response.add(new AdsorbateResponse(adsorbate));
        }
        return response;
    }

    @GetMapping(value = "/buscar")
    public List<AdsorbateResponse> searchAdsorbates(@RequestParam(name="nombre",required = false) String name, @RequestParam(name="cargaIon",required = false) Integer ionCharge, @RequestHeader(name ="Authorization", required = false) String authHeader,@RequestHeader(value = "User-Agent") String userAgent){
        AdsorbateFilter filter = new AdsorbateFilter(name, ionCharge);
        List<AdsorbateResponse> response = new ArrayList<>();
        for (Adsorbate adsorbate: adsorbateService.search(filter,jwtUtils.isAnonymous(authHeader, userAgent))) {
            response.add(new AdsorbateResponse(adsorbate));
        }
        return response;

    }

    @GetMapping(value = "/buscar/nombre")
    public List<AdsorbateNameResponse> searchAdsorbatesName(@RequestParam(name="nombre",required = false) String name, @RequestParam(name="idAdsorbente",required = false) Long adsorbentId, @RequestHeader(name ="Authorization", required = false) String authHeader,@RequestHeader(value = "User-Agent") String userAgent){
        List<AdsorbateNameResponse> adsorbatesName = new ArrayList<>();
        AdsorbateFilter filter = new AdsorbateFilter(name, adsorbentId);
        for (Adsorbate adsorbate : adsorbateService.search(filter,jwtUtils.isAnonymous(authHeader, userAgent))) {
            adsorbatesName.add(new AdsorbateNameResponse(adsorbate));
        }
        return adsorbatesName;
    }

    @GetMapping(value = "/{id}")
    public AdsorbateResponse getAdsorbate(@PathVariable Long id, @RequestHeader(name ="Authorization", required = false) String authHeader, @RequestHeader(value = "User-Agent") String userAgent) {
        try {
            return new AdsorbateResponse(adsorbateService.getById(id,jwtUtils.isAnonymous(authHeader, userAgent)));
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
