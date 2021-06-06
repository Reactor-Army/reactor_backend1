package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdsorbateRepository extends CrudRepository<Adsorbate,Long> , AdsorbateRepositoryCustom {

    Optional<Adsorbate> findByIonNameAndIonChargeAndIonRadius(String ionName, Integer ionCharge, Float ionRadius);

    Optional<Adsorbate> findByFormula(String formula);

    Optional<Adsorbate> findByFormulaAndIonChargeText(String formula, String ionChargeText);

    Optional<Adsorbate> findByNameIUPACNormalized(String nameIUPACNormalized);

    Optional<Adsorbate> findByNameIUPACNormalizedAndIdNot(String nameIUPACNormalized, long id);

}
