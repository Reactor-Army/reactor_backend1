package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.service.utils.FormulaParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsorbateService {

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private FormulaParserService formulaParserService;

    public Adsorbate createAdsorbate(AdsorbateRequest request){
        return saveAdsorbate(new Adsorbate(request));
    }

    public Adsorbate updateAdsorbate(AdsorbateRequest request) throws ComponentNotFoundException {
        Optional<Adsorbate> adsorbate = adsorbateRepository.findById(request.getId());
        if(adsorbate.isPresent()){
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

    public List<Adsorbate> getAll(){
        return (List<Adsorbate>) adsorbateRepository.findAll();
    }

    public List<Adsorbate> search(AdsorbateFilter filter){
        return adsorbateRepository.getAll(filter);
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





}
