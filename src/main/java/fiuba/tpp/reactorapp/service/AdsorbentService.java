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

import java.util.Collections;
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

    public List<Adsorbent> getAll(){
        return (List<Adsorbent>) adsorbentRepository.findAll();
    }

    public List<Adsorbent> search(AdsorbentFilter filter){
         return adsorbentRepository.getAll(filter);
    }

    public Adsorbent getById(Long id) throws ComponentNotFoundException{
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(id);
        if(adsorbent.isPresent()){
            return adsorbent.get();
        }
        throw new ComponentNotFoundException();
    }

    public Integer getAdsorbentProcessCount(Long id){
        return processRepository.getByAdsorbents(Collections.singletonList(id)).size();
    }

}

