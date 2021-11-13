package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.service.chemicalmodels.ThomasModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ThomasModelService implements  ModelService{

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private BreakCurvesDataService breakCurvesDataService;

    public ThomasResponse thomasEvaluation(List<ChemicalObservation> chemicalObs, ThomasRequest request) throws JsonProcessingException {
        List<Observation> observations = getObservationsPoints(chemicalObs);

        ThomasModel thomasModel = new ThomasModel(observations,request.getSorbenteReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());


        ThomasResponse response = thomasModel.calculate(seedService.generateSeedForThomas(observations));
        response.setThomasConstant(mathService.round(response.getThomasConstant()));
        response.setMaxConcentration(mathService.round(response.getMaxConcentration()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        response.setDataId(breakCurvesDataService.persistBreakCurvesData(request,response, EModel.THOMAS));
        return response;
    }

    public double calculateArea(ThomasRequest request,ThomasResponse response, double upperLimit){
        ThomasModel model = new ThomasModel(response.getObservations(),request.getSorbenteReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());
        double result = model.integrate(response.getThomasConstant(), response.getMaxConcentration(),upperLimit);

        return mathService.round(result);
    }
}
