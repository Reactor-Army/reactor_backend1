package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.repository.AdsorbenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdsorbenteService {

    @Autowired
    private AdsorbenteRepository adsorbenteRepository;

    public Adsorbente createAdsorbente(AdsorbenteRequest request){
        return adsorbenteRepository.save(new Adsorbente(request));
    }

    public List<Adsorbente> getAll(){
        return (List<Adsorbente>) adsorbenteRepository.findAll();
    }
}
