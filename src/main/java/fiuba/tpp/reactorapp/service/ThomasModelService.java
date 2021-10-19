package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesThomasDTO;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.service.chemicalmodels.ThomasModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class ThomasModelService implements  ModelService{

    @Autowired
    private MathService mathService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    public ThomasResponse thomasEvaluation(List<ChemicalObservation> chemicalObs, ThomasRequest request) throws JsonProcessingException {
        List<Observation> observations = getObservationsPoints(chemicalObs);

        ThomasModel thomasModel = new ThomasModel(observations,request.getSorbenteReactor(),request.getCaudalVolumetrico(),request.getConcentracionInicial());


        ThomasResponse response = thomasModel.calculate(seedService.generateSeedForThomas(observations));
        response.setThomasConstant(mathService.round(response.getThomasConstant()));
        response.setMaxConcentration(mathService.round(response.getMaxConcentration()));
        response.setObservations(observations);
        response.setRms(mathService.round(response.getRms()));
        response.setDataId(persistBreakCurvesThomas(request,response));
        return response;
    }


    private Long persistBreakCurvesThomas(ThomasRequest request, ThomasResponse response) throws JsonProcessingException {
        BreakCurvesThomasDTO dto = new BreakCurvesThomasDTO(request,response);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(dto);
            BreakCurvesData bcData =  new BreakCurvesData(EModel.THOMAS,data, Calendar.getInstance().getTime());
            return breakCurvesDataRepository.save(bcData).getId();
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

}
