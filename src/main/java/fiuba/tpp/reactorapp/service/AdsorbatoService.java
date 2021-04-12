package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.repository.AdsorbatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdsorbatoService {

    @Autowired
    private AdsorbatoRepository adsorbatoRepository;

    public Adsorbato createAdsorbato(AdsorbatoRequest request){
        return adsorbatoRepository.save(new Adsorbato(request));
    }

    public List<Adsorbato> getAll(){
        return (List<Adsorbato>) adsorbatoRepository.findAll();
    }


}
