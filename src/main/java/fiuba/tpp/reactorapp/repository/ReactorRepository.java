package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReactorRepository extends CrudRepository<Reactor,Long> {

    Optional<Reactor> findByAdsorbenteAndAdsorbatoAndQmaxAndTiempoEquilibrioAndTemperaturaAndPhinicial(Adsorbente adsorbente, Adsorbato adsorbato, Float qMax, Float tiempoEquilibrio, Float temperatura, Float pH);
}
