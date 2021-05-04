package fiuba.tpp.reactorapp.service.load;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
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
        boolean nuevoSorbente= false;
        boolean nuevoSorbato = false;

        XSSFRow row = worksheet.getRow(i);

        if(row.getCell(0) != null) {
            String nombreAdsorbente = row.getCell(0).getStringCellValue();
            String sizeAdsorbente = row.getCell(1).getStringCellValue();
            String nombreIon = row.getCell(2).getStringCellValue();

            String cargaIonText = row.getCell(3).getStringCellValue();
            StringBuilder cargaBuilder = new StringBuilder();
            cargaBuilder.append(cargaIonText);
            cargaBuilder.reverse();
            Integer cargaIon = Integer.valueOf(cargaBuilder.toString());

            Float radioIonico = (float) row.getCell(4).getNumericCellValue();
            Float qMax = (float) row.getCell(5).getNumericCellValue();
            Float tiempoEquilibrio = (float) row.getCell(6).getNumericCellValue();
            Float temperatura = (float) row.getCell(7).getNumericCellValue();
            Float pHInicial = (float) row.getCell(8).getNumericCellValue();
            Float sBet = (float) row.getCell(9).getNumericCellValue();
            Float vBet = (float) row.getCell(10).getNumericCellValue();
            Float pHCargaCero = (float) row.getCell(11).getNumericCellValue();

            String complejacion = row.getCell(12).getStringCellValue();
            boolean complejacionBool = stringToBoolean(complejacion);

            String intercambio = row.getCell(13).getStringCellValue();
            boolean intercambioBool = stringToBoolean(intercambio);

            String reaccion = row.getCell(14).getStringCellValue();
            boolean reaccionBool = stringToBoolean(reaccion);

            Float limite = (float) row.getCell(15).getNumericCellValue();

            String observaciones = row.getCell(16).getStringCellValue();
            String fuente = row.getCell(17).getStringCellValue();

            Optional<Adsorbent> adsorbente = adsorbentRepository.findByNameAndAndParticleSize(nombreAdsorbente, sizeAdsorbente);

            Adsorbent nuevoAdsorbent = new Adsorbent();
            if (!adsorbente.isPresent()) {
                nuevoAdsorbent.setName(nombreAdsorbente);
                nuevoAdsorbent.setParticleSize(sizeAdsorbente);
                nuevoAdsorbent.setpPHZeroCharge(pHCargaCero);
                nuevoAdsorbent.setsBet(sBet);
                nuevoAdsorbent.setvBet(vBet);
                adsorbentRepository.save(nuevoAdsorbent);

                nuevoSorbente = true;
            }

            Optional<Adsorbate> adsorbato = adsorbateRepository.findByIonNameAndIonChargeAndIonRadius(nombreIon, cargaIon, radioIonico);

            Adsorbate newAdsorbate = new Adsorbate();
            if (!adsorbato.isPresent()) {
                newAdsorbate.setIonName(nombreIon);
                newAdsorbate.setIonCharge(cargaIon);
                newAdsorbate.setIonRadius(radioIonico);
                newAdsorbate.setDischargeLimit(limite);
                adsorbateRepository.save(newAdsorbate);

                nuevoSorbato = true;
            }

            Adsorbate sorbato = (nuevoSorbato ? newAdsorbate : adsorbato.get());
            Adsorbent sorbente = (nuevoSorbente ? nuevoAdsorbent : adsorbente.get());

            Optional<Process> reactor = processRepository.findByAdsorbentAndAdsorbateAndQmaxAndEquilibriumTimeAndTemperatureAndInitialPH(sorbente, sorbato, qMax, tiempoEquilibrio, temperatura, pHInicial);

            if (!reactor.isPresent()) {
                Process nuevoProcess = new Process();
                nuevoProcess.setAdsorbent(sorbente);
                nuevoProcess.setAdsorbate(sorbato);
                nuevoProcess.setComplexation(complejacionBool);
                nuevoProcess.setIonicInterchange(intercambioBool);
                nuevoProcess.setChemicalReaction(reaccionBool);
                nuevoProcess.setSource(fuente);
                nuevoProcess.setObservation(observaciones);
                nuevoProcess.setInitialPH(pHInicial);
                nuevoProcess.setQmax(qMax);
                nuevoProcess.setTemperature(temperatura);
                nuevoProcess.setEquilibriumTime(tiempoEquilibrio);

                processRepository.save(nuevoProcess);
            }
        }
    }

    private boolean stringToBoolean(String string){
        return string.equals("si");
    }


}
