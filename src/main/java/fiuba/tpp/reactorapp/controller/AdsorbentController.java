package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateAdsorbentException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentNameResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ProcessCountResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.service.AdsorbentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private JwtUtils jwtUtils;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value= "")
    public AdsorbentResponse createAdsorbent(@RequestBody AdsorbentRequest request) {
        AdsorbentResponse response = null;
        try{
            validateAdsorbent(request);
            response = new AdsorbentResponse(adsorbentService.createAdsorbent(request));
        } catch (InvalidRequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_ADSORBENT.getMessage(), e);
        } catch (DuplicateAdsorbentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_ADSORBENT.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value= "/{id}")
    public AdsorbentResponse updateAdsorbent(@PathVariable Long id, @RequestBody AdsorbentRequest request) {
        AdsorbentResponse response = null;
        try{
            response = new AdsorbentResponse(adsorbentService.updateAdsorbent(id, request));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.ADSORBENT_NOT_FOUND.getMessage(), e);
        }catch (DuplicateAdsorbentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_ADSORBENT.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value= "/{id}")
    public void deleteAdsorbent(@PathVariable Long id){
        try {
            adsorbentService.deleteAdsorbent(id);
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.ADSORBENT_NOT_FOUND.getMessage(), e);
        } catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @GetMapping(value = "")
    public List<AdsorbentResponse> getAdsorbents(@RequestHeader(name ="Authorization", required = false) String authHeader){
        List<AdsorbentResponse> adsorbents = new ArrayList<>();
        for (Adsorbent adsorbent : adsorbentService.getAdsorbents(jwtUtils.isAnonymous(authHeader))) {
            adsorbents.add(new AdsorbentResponse(adsorbent));
        }
        return adsorbents;
    }

    @GetMapping(value = "/buscar")
    public List<AdsorbentResponse> searchAdsorbents(@RequestParam(name="nombre",required = false) String name,@RequestHeader(name ="Authorization", required = false) String authHeader){
        List<AdsorbentResponse> adsorbents = new ArrayList<>();
        AdsorbentFilter filter = new AdsorbentFilter(name);
        for (Adsorbent adsorbent : adsorbentService.search(filter, jwtUtils.isAnonymous(authHeader))) {
            adsorbents.add(new AdsorbentResponse(adsorbent));
        }
        return adsorbents;
    }

    @GetMapping(value = "/buscar/nombre")
    public List<AdsorbentNameResponse> searchAdsorbentsName(@RequestParam(name="nombre",required = false) String name,@RequestParam(name="idAdsorbato",required = false) Long adsorbateId, @RequestHeader("Authorization") String authHeader){
        List<AdsorbentNameResponse> adsorbentsNames = new ArrayList<>();
        AdsorbentFilter filter = new AdsorbentFilter(name, adsorbateId);
        for (Adsorbent adsorbent : adsorbentService.search(filter, jwtUtils.isAnonymous(authHeader))) {
            adsorbentsNames.add(new AdsorbentNameResponse(adsorbent));
        }
        return adsorbentsNames;
    }

    @GetMapping(value = "/{id}")
    public AdsorbentResponse getAdsorbent(@PathVariable Long id,@RequestHeader(name ="Authorization", required = false) String authHeader) {
        try {
            return new AdsorbentResponse(adsorbentService.getById(id,jwtUtils.isAnonymous(authHeader)));
        } catch (ComponentNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.ADSORBENT_NOT_FOUND.getMessage(), e);
        }
    }

    @GetMapping(value = "/{id}/cantidad-procesos")
    public ProcessCountResponse getAdsorbentProcessCount(@PathVariable Long id) {
        return new ProcessCountResponse(adsorbentService.getAdsorbentProcessCount(id));
    }

    private void validateAdsorbent(AdsorbentRequest request) throws InvalidRequestException {
        if(request.getName() == null || request.getName().isEmpty()) throw new InvalidRequestException();
    }
}
