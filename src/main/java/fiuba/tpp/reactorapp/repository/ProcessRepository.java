package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProcessRepository extends CrudRepository<Process,Long>, ReactorRepositoryCustom {

    Optional<Process> findByAdsorbentAndAdsorbateAndQmaxAndTiempoEquilibrioAndTemperaturaAndPhinicial(Adsorbent adsorbent, Adsorbate adsorbate, Float qMax, Float tiempoEquilibrio, Float temperatura, Float pH);
}
