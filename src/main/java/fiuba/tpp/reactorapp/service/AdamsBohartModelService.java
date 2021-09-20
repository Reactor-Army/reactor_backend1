package fiuba.tpp.reactorapp.service;

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

    public AdamsBohartResponse adamsBohartEvaluation(List<ChemicalObservation> chemicalObs, AdamsBohartRequest request){
        List<Observation> observations = getObservationsPoints(chemicalObs);

        AdamsBohartModel model = new AdamsBohartModel(observations,request.getVelocidadLineal(),request.getAlturaLechoReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());

        AdamsBohartResponse response = model.calculate(seedService.generateSeedForAdamsBohart(observations));
        response.setAdamsBohartConstant(mathService.round(response.getAdamsBohartConstant()));
        response.setMaxAbsorptionCapacity(mathService.round(response.getMaxAbsorptionCapacity()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        return response;
    }
}
