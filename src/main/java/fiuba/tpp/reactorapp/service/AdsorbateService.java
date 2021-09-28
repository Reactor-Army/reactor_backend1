package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateIUPACNameException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.service.utils.FormulaParserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AdsorbateService {

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private FormulaParserService formulaParserService;

    public Adsorbate createAdsorbate(AdsorbateRequest request) throws DuplicateIUPACNameException {
        Optional<Adsorbate> adsorbate = adsorbateRepository.findByNameIUPACNormalized(normalizeText(request.getNameIUPAC()));
        if(adsorbate.isPresent()){
            throw new DuplicateIUPACNameException();
        }
        return saveAdsorbate(new Adsorbate(request));
    }

    public Adsorbate updateAdsorbate(Long adsorbateId, AdsorbateRequest request) throws ComponentNotFoundException, DuplicateIUPACNameException {
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(adsorbateId);
        if(adsorbate.isPresent()){
            Optional<Adsorbate> duplicateAdsorbate = adsorbateRepository.findByNameIUPACNormalizedAndIdNot(normalizeText(request.getNameIUPAC()), adsorbateId);
            if(duplicateAdsorbate.isPresent()){
                throw new DuplicateIUPACNameException();
            }
            return saveAdsorbate(adsorbate.get().update(request));
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

    public List<Adsorbate> getAll(Boolean isAnonymous){
        List<Adsorbate> adsorbates = getAllAdsorbates(isAnonymous);
        adsorbates.sort(Comparator.comparing(Adsorbate::getIonName));
        return adsorbates;
    }

    public List<Adsorbate> search(AdsorbateFilter filter, Boolean isAnonymous){
        return adsorbateRepository.getAll(filter, isAnonymous);
    }

    public Adsorbate getById(Long id, Boolean isAnonymous) throws ComponentNotFoundException {
        try{
            return adsorbateRepository.getAdsorbate(id, isAnonymous);
        }catch(Exception e){
            throw new ComponentNotFoundException();
        }
    }

    private List<Adsorbate> getAllAdsorbates(Boolean isAnonymous){
        return adsorbateRepository.getAll(new AdsorbateFilter(), isAnonymous);
    }

    public Long getAdsorbateProcessCount(Long id){
        return processRepository.getAdsorbateProcessCount(id);
    }

    private Adsorbate saveAdsorbate(Adsorbate adsorbate){
        return adsorbateRepository.save(parseFormula(adsorbate));
    }

    private Adsorbate parseFormula(Adsorbate adsorbate){
        adsorbate.setIonChargeText(formulaParserService.parseIonChargeText(adsorbate.getIonCharge()));
        if(adsorbate.getFormula() != null && !adsorbate.getFormula().isEmpty() && containsSign(adsorbate.getFormula())){
            adsorbate.setFormula(formulaParserService.parseFormula(adsorbate.getFormula(),adsorbate.getIonChargeText()));
        }
        return adsorbate;
    }

    private boolean containsSign(String formula){
        return (formula.contains("+") || formula.contains("-"));
    }

    private String normalizeText(String text){
        return StringUtils.stripAccents(text.toLowerCase());
    }





}
