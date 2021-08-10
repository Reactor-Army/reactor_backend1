package fiuba.tpp.reactorapp.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadinsCSVException;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
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
    private MathService mathService;

    public RegressionResult calculateByThomas(ThomasRequest request){
        List<Observation> observations = parseCSV(request.getObservations());

        return mathService.calculateRegression(observations);
    }

    private List<Observation> parseCSV(MultipartFile file){
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<Observation> csvToBean = new CsvToBeanBuilder<Observation>(reader)
                    .withType(Observation.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();

        }catch (IOException e) {
            throw new ErrorReadinsCSVException();
        }
    }
}
