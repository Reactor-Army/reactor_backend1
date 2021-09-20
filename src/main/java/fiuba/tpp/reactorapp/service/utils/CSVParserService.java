package fiuba.tpp.reactorapp.service.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservationCSV;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CSVParserService {

    private static final String COMMA = ",";
    private static final String BLANK_SPACE = " ";
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final Integer EXCEL_COLUMNS= 2;

    private static final String REGEX_CONCENTRATION = ".*(c0|co).*";
    private static final String HEADER_VOLUME_CONCENTRATION = "VolumenEfluente,C/C0" + System.lineSeparator();
    private static final String HEADER_CONCENTRATION_VOLUME = "C/C0,VolumenEfluente" + System.lineSeparator();

    public List<ChemicalObservation> parse(MultipartFile file){
        InputStream inputStream = getInputStreamCSV(file);

        return parseCSV(inputStream);
    }

    private List<ChemicalObservation> parseCSV(InputStream input){
        List<ChemicalObservation> chemicalObservations = new ArrayList<>();
        try (Reader reader = new BufferedReader(new StringReader(formatInputHeaders(input)))) {

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

        }catch(Exception e){
            throw new InvalidCSVFormatException();
        }
    }

    private String formatInputHeaders(InputStream input){
        String result = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(input))){
            String firstLine = reader.readLine();
            String[] headers = firstLine.split(",");
            String header ="";

            if(headers[0].toLowerCase().matches(REGEX_CONCENTRATION)){
                header = HEADER_CONCENTRATION_VOLUME;
            }else{
                header = HEADER_VOLUME_CONCENTRATION;
            }
            String values = "";
            while(reader.ready()){
                values = values.concat(reader.readLine()).concat(System.lineSeparator());
            }

            result = header.concat(values);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private InputStream getInputStreamCSV(MultipartFile file){
        InputStream inputStream;
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try{
            if(extension.equalsIgnoreCase(XLS) || extension.equalsIgnoreCase(XLSX)){
                inputStream = convertToCSV(file.getInputStream());
            }else{
                inputStream = file.getInputStream();
            }
        }catch(Exception e){
            throw new ErrorReadingCSVException();
        }

        return inputStream;

    }

    private InputStream convertToCSV(InputStream inputStream) throws IOException, InvalidFormatException {
        try(Workbook wb = WorkbookFactory.create(inputStream)){
            Sheet sheet = wb.getSheetAt(0);

            StringBuilder builder = new StringBuilder();
            Row row = null;
            for (int i = 0; i < sheet.getLastRowNum()+1; i++) {
                row = sheet.getRow(i);
                StringBuilder builderRow = new StringBuilder();
                for (int j = 0; j < EXCEL_COLUMNS; j++) {
                    if(row.getCell(j)==null) {
                        builderRow.append(BLANK_SPACE+ COMMA);
                    }else {
                        builderRow.append(row.getCell(j)+ COMMA);
                    }
                }
                builder.append(builderRow.substring(0, builderRow.length() - 1)).append(System.lineSeparator());
            }
            return new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
