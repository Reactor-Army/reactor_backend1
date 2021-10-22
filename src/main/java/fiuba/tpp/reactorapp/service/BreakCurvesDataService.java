package fiuba.tpp.reactorapp.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesAdamsDTO;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesThomasDTO;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesYoonNelsonDTO;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class BreakCurvesDataService {


    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    public <T,G> Long persistBreakCurvesData(T request, G response, EModel eModel) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String data = "";
        switch (eModel){
            case THOMAS:
                BreakCurvesThomasDTO dtoThomas = new BreakCurvesThomasDTO((ThomasRequest) request,(ThomasResponse) response);
                data = mapper.writeValueAsString(dtoThomas);
                break;
            case ADAMS_BOHART:
                BreakCurvesAdamsDTO dtoAdams = new BreakCurvesAdamsDTO((AdamsBohartRequest) request,(AdamsBohartResponse) response);
                data = mapper.writeValueAsString(dtoAdams);
                break;
            case YOON_NELSON:
                BreakCurvesYoonNelsonDTO dtoYoonNelson = new BreakCurvesYoonNelsonDTO((YoonNelsonRequest) request, (YoonNelsonResponse) response);
                data = mapper.writeValueAsString(dtoYoonNelson);
                break;
        }
        BreakCurvesData bcData =  new BreakCurvesData(eModel,data, Calendar.getInstance().getTime());
        bcData.setFree(false);
        return breakCurvesDataRepository.save(bcData).getId();
    }
}
