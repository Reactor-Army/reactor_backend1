package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;

import java.util.List;

public interface ProcessRepositoryCustom {

    List<Process> getAll(ProcessFilter filter);

    List<Process> getByAdsorbates(List<Long> adsorbatesIds);
}
