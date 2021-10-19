package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesYoonNelsonDTO;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.service.chemicalmodels.YoonNelsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class YoonNelsonModelService implements ModelService {

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    public YoonNelsonResponse yoonNelsonEvaluation(List<ChemicalObservation> chemicalObs, YoonNelsonRequest request) throws JsonProcessingException {
        List<Observation> observations = getObservationsPoints(chemicalObs);

        YoonNelsonModel model = new YoonNelsonModel(observations,request.getCaudalVolumetrico());

        YoonNelsonResponse response = model.calculate(seedService.generateSeedForYoonNelson(observations));
        response.setYoonNelsonConstant(mathService.round(response.getYoonNelsonConstant()));
        response.setTimeFiftyPercent(mathService.round(response.getTimeFiftyPercent()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        response.setDataId(persistBreakCurvesYoonNelson(request,response));
        return response;
    }

    private Long persistBreakCurvesYoonNelson(YoonNelsonRequest request, YoonNelsonResponse response) throws JsonProcessingException {
        BreakCurvesYoonNelsonDTO dto = new BreakCurvesYoonNelsonDTO(request,response);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(dto);
            BreakCurvesData bcData =  new BreakCurvesData(EModel.YOON_NELSON,data, Calendar.getInstance().getTime());
            return breakCurvesDataRepository.save(bcData).getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
