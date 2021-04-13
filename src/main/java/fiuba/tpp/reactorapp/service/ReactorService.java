package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import fiuba.tpp.reactorapp.repository.AdsorbatoRepository;
import fiuba.tpp.reactorapp.repository.AdsorbenteRepository;
import fiuba.tpp.reactorapp.repository.ReactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReactorService {

    @Autowired
    private ReactorRepository reactorRepository;

    @Autowired
    private AdsorbatoRepository adsorbatoRepository;

    @Autowired
    private AdsorbenteRepository adsorbenteRepository;

    public Reactor createReactor(ReactorRequest request) throws InvalidReactorException {
        Optional<Adsorbente> adsorbente = adsorbenteRepository.findById(request.getIdAdsorbente());
        Optional<Adsorbato> adsorbato = adsorbatoRepository.findById(request.getIdAdsorbato());
        if(adsorbato.isPresent() && adsorbente.isPresent()){
            return reactorRepository.save(new Reactor(adsorbato.get(),adsorbente.get(),request));
        }
        throw new InvalidReactorException();
    }

    public List<Reactor> getAll(){
        return (List<Reactor>) reactorRepository.findAll();
    }
}
