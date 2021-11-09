package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.service.chemicalmodels.AdamsBohartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdamsBohartModelService implements ModelService{

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private BreakCurvesDataService breakCurvesDataService;

    public AdamsBohartResponse adamsBohartEvaluation(List<ChemicalObservation> chemicalObs, AdamsBohartRequest request) throws JsonProcessingException {
        List<Observation> observations = getObservationsPoints(chemicalObs);

        AdamsBohartModel model = new AdamsBohartModel(observations,request.getVelocidadLineal(),request.getAlturaLechoReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());

        AdamsBohartResponse response = model.calculate(seedService.generateSeedForAdamsBohart(observations));
        response.setAdamsBohartConstant(mathService.round(response.getAdamsBohartConstant()));
        response.setMaxAbsorptionCapacity(mathService.round(response.getMaxAbsorptionCapacity()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        response.setDataId(breakCurvesDataService.persistBreakCurvesData(request,response, EModel.ADAMS_BOHART));
        return response;
    }

    public double calculateArea(AdamsBohartRequest request, AdamsBohartResponse response){
        AdamsBohartModel model = new AdamsBohartModel(response.getObservations(),request.getVelocidadLineal(),request.getAlturaLechoReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());
        double result = model.integrate(response.getAdamsBohartConstant(), response.getMaxAbsorptionCapacity(), response.getObservations());

        return mathService.round(result);
    }
}
