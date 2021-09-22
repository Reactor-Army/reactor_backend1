package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.service.chemicalmodels.YoonNelsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoonNelsonModelService implements ModelService {

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    public YoonNelsonResponse yoonNelsonEvaluation(List<ChemicalObservation> chemicalObs, YoonNelsonRequest request){
        List<Observation> observations = getObservationsPoints(chemicalObs);

        YoonNelsonModel model = new YoonNelsonModel(observations,request.getCaudalVolumetrico());

        YoonNelsonResponse response = model.calculate(seedService.generateSeedForYoonNelson(observations));
        response.setYoonNelsonConstant(mathService.round(response.getYoonNelsonConstant()));
        response.setTimeFiftyPercent(mathService.round(response.getTimeFiftyPercent()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        return response;
    }

}
