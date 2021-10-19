package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesAdamsDTO;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.service.chemicalmodels.AdamsBohartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class AdamsBohartModelService implements ModelService{

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    public AdamsBohartResponse adamsBohartEvaluation(List<ChemicalObservation> chemicalObs, AdamsBohartRequest request) throws JsonProcessingException {
        List<Observation> observations = getObservationsPoints(chemicalObs);

        AdamsBohartModel model = new AdamsBohartModel(observations,request.getVelocidadLineal(),request.getAlturaLechoReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());

        AdamsBohartResponse response = model.calculate(seedService.generateSeedForAdamsBohart(observations));
        response.setAdamsBohartConstant(mathService.round(response.getAdamsBohartConstant()));
        response.setMaxAbsorptionCapacity(mathService.round(response.getMaxAbsorptionCapacity()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        response.setDataId(persistBreakCurvesAdamsBoharth(request,response));
        return response;
    }

    private Long persistBreakCurvesAdamsBoharth(AdamsBohartRequest request, AdamsBohartResponse response) throws JsonProcessingException {
        BreakCurvesAdamsDTO dto = new BreakCurvesAdamsDTO(request,response);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(dto);
        BreakCurvesData bcData =  new BreakCurvesData(EModel.ADAMS_BOHART,data, Calendar.getInstance().getTime());
        return breakCurvesDataRepository.save(bcData).getId();
    }
}
