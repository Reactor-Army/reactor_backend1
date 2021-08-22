package fiuba.tpp.reactorapp.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class BreakCurvesService {

    @Autowired
    private ThomasModelService thomasModelService;

    public ThomasResponse calculateByThomas(ThomasRequest request){
        List<ChemicalObservation> chemicalObservations = parseCSV(request.getObservaciones());

        return thomasModelService.thomasEvaluation(chemicalObservations,request);
    }


    private List<ChemicalObservation> parseCSV(MultipartFile file){
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<ChemicalObservation> csvToBean = new CsvToBeanBuilder<ChemicalObservation>(reader)
                    .withType(ChemicalObservation.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();

        }catch (IOException e) {
            throw new ErrorReadingCSVException();
        }catch(Exception e){
            throw new InvalidCSVFormatException();
        }
    }

}
