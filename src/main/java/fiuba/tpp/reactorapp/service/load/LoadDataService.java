package fiuba.tpp.reactorapp.service.load;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.service.utils.FormulaParserService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class LoadDataService {

    Logger logger = LoggerFactory.getLogger(LoadDataService.class);


    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private FormulaParserService formulaParserService;

    @Value("${reactorapp.loadata}")
    private boolean loadData;

    @Value("${reactorapp.file.location}")
    private String location;


    @EventListener(ApplicationReadyEvent.class)
    public void loadData() throws IOException {

        if(loadData && location != null && !location.isEmpty()){
            FileInputStream file = new FileInputStream(new File(location));

            XSSFSheet worksheet;
            try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
                worksheet = workbook.getSheetAt(0);
            }


            for(int i=3;i<worksheet.getPhysicalNumberOfRows() ;i++) {
               loadRowData(worksheet,i);

            }
            logger.info("Proceso de carga terminado");
        }
    }

    private void loadRowData(XSSFSheet worksheet, int i){
        boolean isNewAdsorbent = false;
        boolean isNewAdsorbate = false;

        XSSFRow row = worksheet.getRow(i);

        if(row.getCell(0) != null && !row.getCell(0).getStringCellValue().isEmpty()) {
            String nameAdsorbent = row.getCell(0).getStringCellValue();
            String sizeAdsorbent = row.getCell(1).getStringCellValue();
            String numberCAS = row.getCell(2).getStringCellValue();
            String formulaAdsorbate = row.getCell(3).getStringCellValue().trim();
            String nameIUPAC = row.getCell(4).getStringCellValue();
            String ionName = row.getCell(5).getStringCellValue();

            Integer ionCharge = 0;
            String ionChargeText = row.getCell(6).getStringCellValue();
            StringBuilder chargeBuilder = new StringBuilder();
            chargeBuilder.append(ionChargeText);
            chargeBuilder.reverse();
            if(!chargeBuilder.toString().isEmpty()){
                ionCharge = Integer.valueOf(chargeBuilder.toString());
            }


            Float ionRadius = (float) row.getCell(7).getNumericCellValue();
            Float qMax = (float) row.getCell(8).getNumericCellValue();
            Float equilibriumTime = (float) row.getCell(9).getNumericCellValue();
            Float kineticConstant = (float) row.getCell(10).getNumericCellValue();
            Float reactionOrder = (float) row.getCell(11).getNumericCellValue();
            Float temperature = (float) row.getCell(12).getNumericCellValue();
            Float initialPH = (float) row.getCell(13).getNumericCellValue();
            Float sBet = (float) row.getCell(14).getNumericCellValue();
            Float vBet = (float) row.getCell(15).getNumericCellValue();
            Float pHZeroCharge = (float) row.getCell(16).getNumericCellValue();

            String complexation = row.getCell(17).getStringCellValue();
            boolean complexationBool = stringToBoolean(complexation);

            String interchange = row.getCell(18).getStringCellValue();
            boolean interchangeBool = stringToBoolean(interchange);

            String reaction = row.getCell(19).getStringCellValue();
            boolean reactionBool = stringToBoolean(reaction);

            Float limit = (float) row.getCell(20).getNumericCellValue();

            String observation = row.getCell(21).getStringCellValue();
            String source = row.getCell(22).getStringCellValue();

            Optional<Adsorbent> adsorbent = adsorbentRepository.findByNameAndParticleSize(nameAdsorbent, sizeAdsorbent);

            Adsorbent newAdsorbent = new Adsorbent();
            if (!adsorbent.isPresent()) {
                newAdsorbent.setName(nameAdsorbent);
                newAdsorbent.setParticleSize(sizeAdsorbent);
                newAdsorbent.setpHZeroCharge(pHZeroCharge);
                newAdsorbent.setsBet(sBet);
                newAdsorbent.setvBet(vBet);
                adsorbentRepository.save(newAdsorbent);

                isNewAdsorbent = true;
            }

            String parsedFormula = formulaParserService.parseFormula(formulaAdsorbate.trim(),ionChargeText.trim());
            Optional<Adsorbate> adsorbate = adsorbateRepository.findByFormulaAndIonChargeText(parsedFormula, ionChargeText);

            Adsorbate newAdsorbate = new Adsorbate();
            if (!adsorbate.isPresent()) {
                newAdsorbate.setIonName(ionName);
                newAdsorbate.setIonCharge(ionCharge);
                newAdsorbate.setIonChargeText(ionChargeText.trim());
                newAdsorbate.setIonRadius(ionRadius);
                newAdsorbate.setDischargeLimit(limit);
                newAdsorbate.setNameIUPAC(nameIUPAC);
                newAdsorbate.setNumberCAS(numberCAS);
                newAdsorbate.setFormula(parsedFormula);
                adsorbateRepository.save(newAdsorbate);

                isNewAdsorbate = true;
            }

            Adsorbate sorbate = (isNewAdsorbate ? newAdsorbate : adsorbate.get());
            Adsorbent sorbent = (isNewAdsorbent ? newAdsorbent : adsorbent.get());

            Optional<Process> reactor = processRepository.findByAdsorbentAndAdsorbateAndQmaxAndEquilibriumTimeAndTemperatureAndInitialPH(sorbent, sorbate, qMax, equilibriumTime, temperature, initialPH);

            if (!reactor.isPresent()) {
                Process nuevoProcess = new Process();
                nuevoProcess.setAdsorbent(sorbent);
                nuevoProcess.setAdsorbate(sorbate);
                nuevoProcess.setComplexation(complexationBool);
                nuevoProcess.setIonicInterchange(interchangeBool);
                nuevoProcess.setChemicalReaction(reactionBool);
                nuevoProcess.setSource(source);
                nuevoProcess.setObservation(observation);
                nuevoProcess.setInitialPH(initialPH);
                nuevoProcess.setQmax(qMax);
                nuevoProcess.setTemperature(temperature);
                nuevoProcess.setEquilibriumTime(equilibriumTime);
                nuevoProcess.setKineticConstant(kineticConstant);
                nuevoProcess.setReactionOrder(reactionOrder);

                processRepository.save(nuevoProcess);
            }
        }
    }

    private boolean stringToBoolean(String string){
        return string.equals("si");
    }

}
