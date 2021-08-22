package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import fiuba.tpp.reactorapp.service.chemicalmodels.ThomasModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThomasModelService {

    @Autowired
    private MathService mathService;

    public ThomasResponse thomasEvaluation(List<ChemicalObservation> chemicalObs, ThomasRequest request){
        List<Observation> observations = getObservationsPoints(chemicalObs);

        ThomasModel thomasModel = new ThomasModel(observations,request.getSorbenteReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());

        ThomasResponse response = thomasModel.calculate();
        response.setThomasConstant(mathService.round(response.getThomasConstant()));
        response.setMaxConcentration(mathService.round(response.getMaxConcentration()));
        response.setObservations(observations);
        return response;
    }



    private List<Observation> getObservationsPoints(List<ChemicalObservation> chemicals){
        List<Observation> observations = new ArrayList<>();
        for (ChemicalObservation chemical: chemicals ) {
            if(chemical.getRelacionConcentraciones() > 0){
                Observation obs = new Observation();
                obs.setX(chemical.getVolumenEfluente());
                obs.setY(chemical.getRelacionConcentraciones());
                observations.add(obs);
            }
        }
        return observations;

    }


}
