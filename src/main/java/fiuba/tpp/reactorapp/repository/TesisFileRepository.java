package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.TesisFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TesisFileRepository extends JpaRepository<TesisFile,Long>, TesisFileRepositoryCustom {
}
