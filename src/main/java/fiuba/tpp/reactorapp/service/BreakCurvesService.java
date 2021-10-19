package fiuba.tpp.reactorapp.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesThomasDTO;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.BreakCurvesDataResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.service.utils.CSVParserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class BreakCurvesService {

    @Autowired
    private ThomasModelService thomasModelService;

    @Autowired
    private YoonNelsonModelService yoonNelsonModelService;

    @Autowired
    private AdamsBohartModelService adamsBohartModelService;

    @Autowired
    private CSVParserService csvParserService;

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    public ThomasResponse calculateByThomas(ThomasRequest request) throws JsonProcessingException {
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return thomasModelService.thomasEvaluation(chemicalObservations,request);
    }

    public YoonNelsonResponse calculateByYoonNelson(YoonNelsonRequest request){
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return yoonNelsonModelService.yoonNelsonEvaluation(chemicalObservations,request);
    }

    public AdamsBohartResponse calculateByAdamsBohart(AdamsBohartRequest request){
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return adamsBohartModelService.adamsBohartEvaluation(chemicalObservations,request);
    }

    public FileTemplateDTO getDataTemplateFile() throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream inputStream = cl.getResourceAsStream("dataFiles/datos.xlsx");
        byte[] bytes = IOUtils.toByteArray(inputStream);

        return new FileTemplateDTO(new ByteArrayResource(bytes),"datos.xlsx", bytes.length);
    }

    public BreakCurvesDataResponse getBreakCurveData(Long id) throws JsonProcessingException {
        Optional<BreakCurvesData> data = breakCurvesDataRepository.findById(id);
        if(data.isPresent()){
            return formatData(data.get());
        }
        throw new ComponentNotFoundException();
    }

    private BreakCurvesDataResponse formatData(BreakCurvesData data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        switch (data.getModel()){
            case THOMAS:
                BreakCurvesThomasDTO dto = mapper.readValue(data.getData(), BreakCurvesThomasDTO.class);
                return new BreakCurvesDataResponse(data,dto);
            default:
                return null;
        }
    }

}
