package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdsorbateRepository extends CrudRepository<Adsorbate,Long> , AdsorbateRepositoryCustom {

    Optional<Adsorbate> findByNombreIonAndCargaIonAndRadioIonico(String nombreIon, Integer cargaIon, Float radioIonico);

}