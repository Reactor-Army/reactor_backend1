package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.entities.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BreakCurvesDataRepository extends JpaRepository<BreakCurvesData,Long> {

    void deleteAllByNameNullAndUploadDateBefore(Date date);

    Optional<BreakCurvesData> findByIdAndNameNotNull(Long id);

    Optional<BreakCurvesData> findByIdAndNameNotNullAndBaselineTrue(Long id);

    List<BreakCurvesData> findAllByProcess(Process process);

    List<BreakCurvesData> findAllByModelAndFreeTrue(EModel model);
}
