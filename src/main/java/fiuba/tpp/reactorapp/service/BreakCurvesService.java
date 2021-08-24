package fiuba.tpp.reactorapp.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservationCSV;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.service.utils.CSVParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreakCurvesService {

    @Autowired
    private ThomasModelService thomasModelService;

    @Autowired
    private YoonNelsonModelService yoonNelsonModelService;

    @Autowired
    private CSVParserService csvParserService;

    public ThomasResponse calculateByThomas(ThomasRequest request){
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return thomasModelService.thomasEvaluation(chemicalObservations,request);
    }

    public YoonNelsonResponse calculateByYoonNelson(YoonNelsonRequest request){
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return yoonNelsonModelService.yoonNelsonEvaluation(chemicalObservations,request);
    }



}
