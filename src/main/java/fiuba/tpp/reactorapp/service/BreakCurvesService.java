package fiuba.tpp.reactorapp.service;


import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.service.utils.CSVParserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    private static final String FILEPATH = "classpath:dataFiles" + File.separator+ "datos.xlsx";

    public ThomasResponse calculateByThomas(ThomasRequest request){
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

}
