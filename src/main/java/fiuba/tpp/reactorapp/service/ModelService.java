package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;

import java.util.ArrayList;
import java.util.List;

public interface ModelService {

    default List<Observation> getObservationsPoints(List<ChemicalObservation> chemicals){
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
