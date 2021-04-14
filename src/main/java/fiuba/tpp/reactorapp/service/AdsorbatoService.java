package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.repository.AdsorbatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsorbatoService {

    @Autowired
    private AdsorbatoRepository adsorbatoRepository;

    public Adsorbato createAdsorbato(AdsorbatoRequest request){
        return adsorbatoRepository.save(new Adsorbato(request));
    }

    public Adsorbato updateAdsorbato(AdsorbatoRequest request) throws ComponentNotFoundException {
        Optional<Adsorbato> adsorbato = adsorbatoRepository.findById(request.getId());
        if(adsorbato.isPresent()){
            return adsorbatoRepository.save(adsorbato.get().update(request));
        }
        throw new ComponentNotFoundException();
    }

    public void deleteAdsorbato(Long id) throws ComponentNotFoundException {
        Optional<Adsorbato> adsorbato = adsorbatoRepository.findById(id);
        if(adsorbato.isPresent()){
            adsorbatoRepository.delete(adsorbato.get());
            return;
        }
        throw new ComponentNotFoundException();

    }

    public List<Adsorbato> getAll(){
        return (List<Adsorbato>) adsorbatoRepository.findAll();
    }




}
