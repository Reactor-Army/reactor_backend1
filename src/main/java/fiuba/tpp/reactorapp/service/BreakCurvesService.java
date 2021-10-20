package fiuba.tpp.reactorapp.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesAdamsDTO;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesThomasDTO;
import fiuba.tpp.reactorapp.model.dto.BreakCurvesYoonNelsonDTO;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidRequestException;
import fiuba.tpp.reactorapp.model.request.BreakCurveDataRequest;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.BreakCurvesDataResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.service.utils.CSVParserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ProcessRepository processRepository;

    public ThomasResponse calculateByThomas(ThomasRequest request) throws JsonProcessingException {
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return thomasModelService.thomasEvaluation(chemicalObservations,request);
    }

    public YoonNelsonResponse calculateByYoonNelson(YoonNelsonRequest request) throws JsonProcessingException {
        List<ChemicalObservation> chemicalObservations = csvParserService.parse(request.getObservaciones());

        return yoonNelsonModelService.yoonNelsonEvaluation(chemicalObservations,request);
    }

    public AdamsBohartResponse calculateByAdamsBohart(AdamsBohartRequest request) throws JsonProcessingException {
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
        Optional<BreakCurvesData> data = breakCurvesDataRepository.findByIdAndNameNotNull(id);
        if(data.isPresent()){
            return formatData(data.get());
        }
        throw new ComponentNotFoundException();
    }

    private BreakCurvesDataResponse formatData(BreakCurvesData data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        switch (data.getModel()){
            case THOMAS:
                BreakCurvesThomasDTO dtoThomas = mapper.readValue(data.getData(), BreakCurvesThomasDTO.class);
                return new BreakCurvesDataResponse(data,dtoThomas.getRequest(), dtoThomas.getResponse());
            case YOON_NELSON:
                BreakCurvesYoonNelsonDTO dtoYoon = mapper.readValue(data.getData(),BreakCurvesYoonNelsonDTO.class);
                return new BreakCurvesDataResponse(data,dtoYoon.getRequest(),dtoYoon.getResponse());
            case ADAMS_BOHART:
                BreakCurvesAdamsDTO dtoAdams = mapper.readValue(data.getData(),BreakCurvesAdamsDTO.class);
                return new BreakCurvesDataResponse(data,dtoAdams.getRequest(),dtoAdams.getResponse());
            default:
                throw new ComponentNotFoundException();
        }
    }

    @Transactional
    public void deleteBreakCurveData(Long id){
        Optional<BreakCurvesData> data = breakCurvesDataRepository.findById(id);
        if(data.isPresent()){
            breakCurvesDataRepository.delete(data.get());
            return;
        }
        throw new ComponentNotFoundException();
    }

    public BreakCurvesDataResponse saveBreakCurveData(Long dataId, BreakCurveDataRequest request) throws JsonProcessingException {
        Optional<BreakCurvesData> data = breakCurvesDataRepository.findById(dataId);

        if(!data.isPresent()) throw new InvalidRequestException();
        BreakCurvesData d = data.get();

        Optional<Process> process = processRepository.findById(request.getProcessId());
        if(!process.isPresent()) throw new InvalidRequestException();

        d.setName(request.getName());
        d.setProcess(process.get());
        breakCurvesDataRepository.save(d);
        return formatData(d);
    }

}
