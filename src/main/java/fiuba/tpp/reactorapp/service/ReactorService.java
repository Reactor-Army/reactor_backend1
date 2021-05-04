package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.filter.ReactorFilter;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
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
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private AdsorbenteRepository adsorbenteRepository;

    public Reactor createReactor(ReactorRequest request) throws InvalidReactorException {
        Optional<Adsorbente> adsorbente = adsorbenteRepository.findById(request.getIdAdsorbente());
        Optional<Adsorbate> adsorbato = adsorbateRepository.findById(request.getIdAdsorbato());
        if(adsorbato.isPresent() && adsorbente.isPresent()){
            return reactorRepository.save(new Reactor(adsorbato.get(),adsorbente.get(),request));
        }
        throw new InvalidReactorException();
    }

    public Reactor updateReactor(ReactorRequest request) throws InvalidReactorException {
        Optional<Adsorbente> adsorbente = adsorbenteRepository.findById(request.getIdAdsorbente());
        Optional<Adsorbate> adsorbato = adsorbateRepository.findById(request.getIdAdsorbato());
        Optional<Reactor> reactor = reactorRepository.findById(request.getId());
        if(adsorbato.isPresent() && adsorbente.isPresent() && reactor.isPresent()){
            return reactorRepository.save(reactor.get().update(adsorbato.get(),adsorbente.get(),request));
        }
        throw new InvalidReactorException();
    }

    public void deleteReactor(Long id) throws ComponentNotFoundException {
        Optional<Reactor> reactor = reactorRepository.findById(id);
        if(reactor.isPresent()){
            reactorRepository.delete(reactor.get());
            return;
        }
        throw new ComponentNotFoundException();
    }


    public List<Reactor> getAll(){
        return (List<Reactor>) reactorRepository.findAll();
    }

    public List<Reactor> search(ReactorFilter filter){ return reactorRepository.getAll(filter);}
}
