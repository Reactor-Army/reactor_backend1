package fiuba.tpp.reactorapp.service.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservationCSV;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVParserService {

    public List<ChemicalObservation> parse(MultipartFile file){
        List<ChemicalObservation> chemicalObservations = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<ChemicalObservationCSV> csvToBean = new CsvToBeanBuilder<ChemicalObservationCSV>(reader)
                    .withType(ChemicalObservationCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ChemicalObservationCSV> csvValues = csvToBean.parse();

            for (ChemicalObservationCSV obsCSV: csvValues) {
                ChemicalObservation obs = new ChemicalObservation();
                obs.setVolumenEfluente(Double.parseDouble(obsCSV.getVolumenEfluente().replace(",",".")));
                obs.setRelacionConcentraciones(Double.parseDouble(obsCSV.getRelacionConcentraciones().replace(",",".")));
                chemicalObservations.add(obs);
            }
            return chemicalObservations;

        }catch (IOException e) {
            throw new ErrorReadingCSVException();
        }catch(Exception e){
            throw new InvalidCSVFormatException();
        }
    }
}
