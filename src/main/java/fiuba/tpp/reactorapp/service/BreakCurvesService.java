package fiuba.tpp.reactorapp.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
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

    @Autowired
    private ThomasModelService thomasModelService;

    public ThomasResponse calculateByThomas(ThomasRequest request){
        List<ChemicalObservation> chemicalObservations = parseCSV(request.getObservaciones());

        RegressionResult regression = mathService.calculateRegression(calculateObservations(chemicalObservations,request.getConcentracionInicial(), request.getCaudalVolumetrico()));

        ThomasResponse response = thomasModelService.calculateThomas(regression,request);
        response.setObservations(getObservationsPoints(chemicalObservations,request.getConcentracionInicial()));

        return response;
    }

    private List<Observation> calculateObservations(List<ChemicalObservation> chemicals, double initialConcentration, double flow){
        List<Observation> observations = new ArrayList<>();
        for (ChemicalObservation chemical: chemicals ) {
            Observation obs = new Observation();
            obs.setX(calculateEffluentVolume(chemical.getTiempo(),flow));
            obs.setY(concentrationLogarithm(chemical.getConcentracionSalida(), initialConcentration));
            observations.add(obs);
        }
        return observations;
    }

    private List<Observation> getObservationsPoints(List<ChemicalObservation> chemicals, double initialConcentration){
        List<Observation> observations = new ArrayList<>();
        for (ChemicalObservation chemical: chemicals ) {
            Observation obs = new Observation();
            obs.setX(chemical.getTiempo());
            obs.setY(mathService.divide(initialConcentration,chemical.getConcentracionSalida()));
            observations.add(obs);
        }
        return observations;

    }

    private double concentrationLogarithm(double concentration, double initialConcentration){
        return mathService.ln((initialConcentration/concentration) - 1);
    }



    private double calculateEffluentVolume(double time, double flow){
        double flowL = flow /1000;
        return time * flowL;
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
