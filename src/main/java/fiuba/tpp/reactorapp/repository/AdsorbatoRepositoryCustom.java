package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;

import java.util.List;

public interface AdsorbatoRepositoryCustom {

    List<Adsorbato> getAll(AdsorbatoFilter filter);
}
