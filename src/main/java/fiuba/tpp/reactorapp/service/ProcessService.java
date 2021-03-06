package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.dto.SearchByAdsorbateDTO;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.ReactorVolumeRequest;
import fiuba.tpp.reactorapp.model.request.SearchByAdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.ProcessResponse;
import fiuba.tpp.reactorapp.model.response.ReactorVolumeResponse;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private MathService mathService;

    public Process createProcess(ProcessRequest request) throws InvalidProcessException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(request.getIdAdsorbent());
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getIdAdsorbate());
        if(adsorbate.isPresent() && adsorbent.isPresent()){
            return processRepository.save(new Process(adsorbate.get(),adsorbent.get(),request));
        }
        throw new InvalidProcessException();
    }

    public Process updateProcess(Long id, ProcessRequest request) throws InvalidProcessException {
        Optional<Adsorbent> adsorbent = adsorbentRepository.findById(request.getIdAdsorbent());
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getIdAdsorbate());
        Optional<Process> process = processRepository.findById(id);
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

    public List<SearchByAdsorbateDTO> searchByAdsorbate(SearchByAdsorbateRequest request, Boolean isAnonymous){
        List<Process> processes = processRepository.getByAdsorbates(request.getAdsorbatesIds(), isAnonymous);
        HashMap<Long, SearchByAdsorbateDTO> result = new HashMap<>();
        for (Process process: processes) {
            Long idAdsorbent = process.getAdsorbent().getId();
            if(result.containsKey(idAdsorbent)){
                addProcessToResult(result.get(idAdsorbent),process);
            }else{
                result.put(idAdsorbent,new SearchByAdsorbateDTO(process));
            }
        }
        List<SearchByAdsorbateDTO> orderResult= new ArrayList<>(result.values());
        orderResult.sort(Comparator.comparing(SearchByAdsorbateDTO::getMaxQmax).reversed());
        return orderResult;
    }

    private void addProcessToResult(SearchByAdsorbateDTO result, Process process){
        result.getProcesses().add(process);
        if(!result.getRemoveAdsorbates().contains(process.getAdsorbate())){
            result.getRemoveAdsorbates().add(process.getAdsorbate());
        }
    }


    public List<Process> getProcesses(Boolean isAnonymous){
        return processRepository.getProcesses(new ProcessFilter(),isAnonymous);
    }

    public Process getById(Long id, Boolean isAnonymous) throws ComponentNotFoundException {
        try{
            return processRepository.getProcess(id,isAnonymous);
        }catch(Exception e){
            throw new ComponentNotFoundException();
        }
    }

    public List<Process> search(ProcessFilter filter,Boolean isAnonymous){ return processRepository.getProcesses(filter, isAnonymous);}

    public ReactorVolumeResponse calculateVolume(Long id, ReactorVolumeRequest request) throws ComponentNotFoundException , InvalidProcessException{
        Process process = getById(id,false);
        validateProcessKineticInformation(process);

        double reactorVolume;

        if(process.getReactionOrder() == 1){
            reactorVolume = mathService.resolveFirstOrder(process,request.getInitialConcentration(), request.getFinalConcentration(), request.getFlow());
        }else{
            reactorVolume = mathService.resolveSecondOrder(process,request.getInitialConcentration(), request.getFinalConcentration(), request.getFlow());
        }
        return new ReactorVolumeResponse(new ProcessResponse(process),reactorVolume);
    }

    private void validateProcessKineticInformation(Process process){
        if(process.getKineticConstant() == null) throw new InvalidProcessException();
        if(process.getReactionOrder() == null ) throw new InvalidProcessException();
    }
}
