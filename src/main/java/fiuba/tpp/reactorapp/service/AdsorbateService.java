package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsorbateService {

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    public Adsorbate createAdsorbate(AdsorbateRequest request){
        return adsorbateRepository.save(new Adsorbate(request));
    }

    public Adsorbate updateAdsorbate(AdsorbateRequest request) throws ComponentNotFoundException {
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getId());
        if(adsorbate.isPresent()){
            return adsorbateRepository.save(adsorbate.get().update(request));
        }
        throw new ComponentNotFoundException();
    }

    public void deleteAdsorbate(Long id) throws ComponentNotFoundException {
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(id);
        if(adsorbate.isPresent()){
            adsorbateRepository.delete(adsorbate.get());
            return;
        }
        throw new ComponentNotFoundException();

    }

    public List<Adsorbate> getAll(){
        return (List<Adsorbate>) adsorbateRepository.findAll();
    }

    public List<Adsorbate> search(AdsorbateFilter filter){
        return adsorbateRepository.getAll(filter);
    }





}
