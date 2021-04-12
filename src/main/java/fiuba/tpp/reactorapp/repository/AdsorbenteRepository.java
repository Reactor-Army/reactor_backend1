package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdsorbenteRepository extends CrudRepository<Adsorbente,Long> {
    Optional<Adsorbente> findByNombreAndAndParticulaT(String nombre, String particulaT);

}
