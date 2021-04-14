package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.repository.AdsorbenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsorbenteService {

    @Autowired
    private AdsorbenteRepository adsorbenteRepository;

    public Adsorbente createAdsorbente(AdsorbenteRequest request){
        return adsorbenteRepository.save(new Adsorbente(request));
    }

    public Adsorbente updateAdsorbente(AdsorbenteRequest request) throws ComponentNotFoundException {
        Optional<Adsorbente> adsorbente = adsorbenteRepository.findById(request.getId());
        if(adsorbente.isPresent()){
            return adsorbenteRepository.save(adsorbente.get().update(request));
        }
        throw new ComponentNotFoundException();
    }

    public void deleteAdsorbente(Long id) throws ComponentNotFoundException {
        Optional<Adsorbente> adsorbente = adsorbenteRepository.findById(id);
        if(adsorbente.isPresent()){
            adsorbenteRepository.delete(adsorbente.get());
            return;
        }
        throw new ComponentNotFoundException();

    }

    public List<Adsorbente> getAll(){
        return (List<Adsorbente>) adsorbenteRepository.findAll();
    }
}
