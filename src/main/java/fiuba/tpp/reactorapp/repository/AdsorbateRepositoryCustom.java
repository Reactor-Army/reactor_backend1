package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;

import java.util.List;

public interface AdsorbateRepositoryCustom {

    List<Adsorbate> getAll(AdsorbateFilter filter);
}
