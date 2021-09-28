package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;

import java.util.List;

public interface AdsorbentRepositoryCustom {

    List<Adsorbent> getAll(AdsorbentFilter filter, Boolean isAnonymous);

    Adsorbent getAdsorbent(Long id, Boolean isAnonymous);
}
