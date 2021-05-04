package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsorbentService {

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    public Adsorbent createAdsorbent(AdsorbentRequest request){
        return adsorbentRepository.save(new Adsorbent(request));
    }

    public Adsorbent updateAdsorbent(AdsorbentRequest request) throws ComponentNotFoundException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(request.getId());
        if(adsorbent.isPresent()){
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
}
