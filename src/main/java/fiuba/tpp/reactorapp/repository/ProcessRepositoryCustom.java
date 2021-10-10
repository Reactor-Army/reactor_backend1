package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;

import java.util.List;

public interface ProcessRepositoryCustom {

    List<Process> getProcesses(ProcessFilter filter, Boolean isAnonymous);

    List<Process> getByAdsorbates(List<Long> adsorbatesIds, Boolean isAnonymous);

    Process getProcess(Long id, Boolean isAnonymous);

    Long getAdsorbateProcessCount(Long adsorbateId);

    Long getAdsorbentProcessCount(Long adsorbentId);

    List<Process> getProcessesByIds(List<Long> processesIds);
}
