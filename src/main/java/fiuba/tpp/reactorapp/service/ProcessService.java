package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    public Process createProcess(ProcessRequest request) throws InvalidProcessException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(request.getIdAdsorbent());
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getIdAdsorbate());
        if(adsorbate.isPresent() && adsorbent.isPresent()){
            return processRepository.save(new Process(adsorbate.get(),adsorbent.get(),request));
        }
        throw new InvalidProcessException();
    }

    public Process updateProcess(ProcessRequest request) throws InvalidProcessException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(request.getIdAdsorbent());
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getIdAdsorbate());
        Optional<Process> process = processRepository.findById(request.getId());
        if(adsorbate.isPresent() && adsorbent.isPresent() && process.isPresent()){
            return processRepository.save(process.get().update(adsorbate.get(),adsorbent.get(),request));
        }
        throw new InvalidProcessException();
    }

    public void deleteProcess(Long id) throws ComponentNotFoundException {
        Optional<Process> process = processRepository.findById(id);
        if(process.isPresent()){
            processRepository.delete(process.get());
            return;
        }
        throw new ComponentNotFoundException();
    }


    public List<Process> getAll(){
        return (List<Process>) processRepository.findAll();
    }

    public List<Process> search(ProcessFilter filter){ return processRepository.getAll(filter);}
}
