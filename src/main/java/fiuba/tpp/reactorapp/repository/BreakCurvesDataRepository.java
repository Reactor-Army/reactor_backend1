package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BreakCurvesDataRepository extends JpaRepository<BreakCurvesData,Long> {

    void deleteAllByNameNullAndUploadDateBefore(Date date);
}
