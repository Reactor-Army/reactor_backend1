package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProcessRepository extends CrudRepository<Process,Long>, ProcessRepositoryCustom {

    Optional<Process> findByAdsorbentAndAdsorbateAndQmaxAndEquilibriumTimeAndTemperatureAndInitialPH(Adsorbent adsorbent, Adsorbate adsorbate, Float qMax, Float equilibriumTime, Float temperature, Float pH);
}
