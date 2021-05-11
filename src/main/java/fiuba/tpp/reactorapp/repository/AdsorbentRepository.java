package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdsorbentRepository extends CrudRepository<Adsorbent,Long>, AdsorbentRepositoryCustom  {

    Optional<Adsorbent> findByNameAndAndParticleSize(String name, String particleSize);

}
