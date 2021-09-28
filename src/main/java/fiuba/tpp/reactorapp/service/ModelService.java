package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;

import java.util.ArrayList;
import java.util.List;

public interface ModelService {

    double MIN_CONCENTRATION = 0.0001;

    default List<Observation> getObservationsPoints(List<ChemicalObservation> chemicals){
        List<Observation> observations = new ArrayList<>();
        for (ChemicalObservation chemical: chemicals ) {
            if(chemical.getRelacionConcentraciones() > 0){
                Observation obs = new Observation();
                obs.setX(chemical.getVolumenEfluente());
                obs.setY(chemical.getRelacionConcentraciones());
                observations.add(obs);
            }
            if(chemical.getRelacionConcentraciones() == 0){
                Observation obs = new Observation();
                obs.setX(chemical.getVolumenEfluente());
                obs.setY(MIN_CONCENTRATION);
                observations.add(obs);
            }
        }
        return observations;

    }
}
