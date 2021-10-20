package fiuba.tpp.reactorapp.scheduler;

import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Component
public class BreakCurvesDataScheduler {

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void cleanBreakCurvesDataJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        breakCurvesDataRepository.deleteAllByNameNullAndUploadDateBefore(calendar.getTime());
    }
}
