package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Reactor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReactorRepository extends CrudRepository<Reactor,Long>, ReactorRepositoryCustom {

    Optional<Reactor> findByAdsorbentAndAdsorbateAndQmaxAndTiempoEquilibrioAndTemperaturaAndPhinicial(Adsorbent adsorbent, Adsorbate adsorbate, Float qMax, Float tiempoEquilibrio, Float temperatura, Float pH);
}
