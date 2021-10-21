package fiuba.tpp.reactorapp.service.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import fiuba.tpp.reactorapp.model.exception.ErrorReadingCSVException;
import fiuba.tpp.reactorapp.model.exception.InvalidCSVFormatException;
import fiuba.tpp.reactorapp.model.request.ChemicalObservation;
import fiuba.tpp.reactorapp.model.request.ChemicalObservationCSV;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

@Service
public class CSVParserService {

    private static final String COMMA = ",";
    private static final String BLANK_SPACE = " ";
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final Integer EXCEL_COLUMNS= 2;

    private static final String HEADER_VOLUME_CONCENTRATION = "VolumenEfluente,C/C0" + System.lineSeparator();
    private static final String HEADER_CONCENTRATION_VOLUME = "C/C0,VolumenEfluente" + System.lineSeparator();

    public List<ChemicalObservation> parse(MultipartFile file){
        InputStream inputStream = getInputStreamCSV(file);

        return parseCSV(inputStream);
    }

    private List<ChemicalObservation> parseCSV(InputStream input){
        List<ChemicalObservation> chemicalObservations = new ArrayList<>();
        try (Reader reader = new BufferedReader(new StringReader(inferHeaders(input)))) {

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

    /**
     * Modifica los headers del archivo de observaciones/datos para que cumplan con el formato que requiere la liberia usada
     * Ademas si no hay headers asume que la primera columna es VolumenEfluente y la segunda C/C0
     * Ademas verifica c0 o co para hallar la columna correcta.
     * El objetivo de este metodo es hacer mas flexible la cantidad de archivos correctos que podemos procesar
     * Dandole mas libertad al usuario
     * @param input
     * @return
     */
    private String inferHeaders(InputStream input){
        String result = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(input))){
            String firstLine = reader.readLine();
            String[] headers = firstLine.split(",");
            String header ="";
            String values = "";
            if(StringUtils.isNumeric(headers[0])){
                values = values.concat(firstLine).concat(System.lineSeparator());
            }
            boolean foundEmptyLine = false;
            while(reader.ready() && !foundEmptyLine){
                String line = reader.readLine();
                foundEmptyLine = emptyLine(line);
                if(!foundEmptyLine){
                    values = values.concat(line).concat(System.lineSeparator());
                }
            }

            if(isConcentrationHeader(headers[0])){
                header = HEADER_CONCENTRATION_VOLUME;
            }else{
                header = HEADER_VOLUME_CONCENTRATION;
            }

            result = header.concat(values);

        } catch (IOException e) {
           throw new InvalidCSVFormatException();
        }
        return result;
    }

    private boolean emptyLine(String line){
        return line.replace(" ","").replace(",","").isEmpty();
    }

    private boolean isConcentrationHeader(String header){
        return header.toLowerCase().contains("co") || header.toLowerCase().contains("c0");
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
                if(row != null){
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

            }
            return new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
