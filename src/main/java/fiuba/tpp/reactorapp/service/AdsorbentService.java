package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateAdsorbentException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AdsorbentService {

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private ProcessRepository processRepository;

    public Adsorbent createAdsorbent(AdsorbentRequest request) throws DuplicateAdsorbentException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findByNameAndParticleSize(request.getName(),request.getParticleSize());
        if(adsorbent.isPresent()){
            throw new DuplicateAdsorbentException();
        }

        return adsorbentRepository.save(new Adsorbent(request));
    }

    public Adsorbent updateAdsorbent(Long id, AdsorbentRequest request) throws ComponentNotFoundException, DuplicateAdsorbentException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(id);
        if(adsorbent.isPresent()){
            Optional<Adsorbent> duplicateAdsorbent = adsorbentRepository.findByNameAndParticleSizeAndIdNot(request.getName(), request.getParticleSize(), id);
            if(duplicateAdsorbent.isPresent()){
                throw new DuplicateAdsorbentException();
            }
            return adsorbentRepository.save(adsorbent.get().update(request));
        }
        throw new ComponentNotFoundException();
    }

    public void deleteAdsorbent(Long id) throws ComponentNotFoundException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(id);
        if(adsorbent.isPresent()){
            adsorbentRepository.delete(adsorbent.get());
            return;
        }
        throw new ComponentNotFoundException();

    }

    public List<Adsorbent> getAdsorbents(Boolean isAnonymous){
        List<Adsorbent> adsorbents = getAllAdsorbents(isAnonymous);
        adsorbents.sort(Comparator.comparing(Adsorbent::getName));
        return adsorbents;
    }

    public List<Adsorbent> search(AdsorbentFilter filter, Boolean isAnonymous){
         return adsorbentRepository.getAdsorbents(filter, isAnonymous);
    }

    public Adsorbent getById(Long id, Boolean isAnonymous) throws ComponentNotFoundException{
        try{
            return adsorbentRepository.getAdsorbent(id,isAnonymous);
        }catch(Exception e){
            throw new ComponentNotFoundException();
        }
    }

    private List<Adsorbent> getAllAdsorbents(Boolean isAnonymous){
        return adsorbentRepository.getAdsorbents(new AdsorbentFilter(),isAnonymous);
    }

    public Long getAdsorbentProcessCount(Long id){
        return processRepository.getAdsorbentProcessCount(id);
    }

}

