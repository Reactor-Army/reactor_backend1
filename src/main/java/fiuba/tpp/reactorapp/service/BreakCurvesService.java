package fiuba.tpp.reactorapp.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadinsCSVException;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class BreakCurvesService {

    @Autowired
    private MathService mathService;

    public ThomasResponse calculateByThomas(ThomasRequest request){
        List<ChemicalObservation> chemicalObservations = parseCSV(request.getObservaciones());

        RegressionResult regression = mathService.calculateRegression(calculateObservations(chemicalObservations,request.getConcentracionInicial()));

        return mathService.calculateThomas(regression,request);
    }


    private List<Observation> calculateObservations(List<ChemicalObservation> chemicals, double initialConcentration){
        List<Observation> observations = new ArrayList<>();
        for (ChemicalObservation chemical: chemicals ) {
            Observation obs = new Observation();
            obs.setX(chemical.getVolumenEfluente());
            obs.setY(concentrationLogarithm(chemical.getConcentracionSalida(), initialConcentration));
            observations.add(obs);
        }
        return observations;
    }

    private double concentrationLogarithm(double concentration, double initialConcentration){
        return mathService.ln((concentration/initialConcentration) - 1);
    }

    private List<ChemicalObservation> parseCSV(MultipartFile file){
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<ChemicalObservation> csvToBean = new CsvToBeanBuilder<ChemicalObservation>(reader)
                    .withType(ChemicalObservation.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();

        }catch (IOException e) {
            throw new ErrorReadinsCSVException();
        }
    }
}
