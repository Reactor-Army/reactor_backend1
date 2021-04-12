package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdsorbatoRepository extends CrudRepository<Adsorbato,Long> {

    Optional<Adsorbato> findByNombreIonAndCargaIonAndRadioIonico(String nombreIon, Float cargaIon, Float RadioIonico);

}
