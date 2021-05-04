package fiuba.tpp.reactorapp.service.load;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbenteRepository;
import fiuba.tpp.reactorapp.repository.ReactorRepository;
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
    private AdsorbenteRepository adsorbenteRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ReactorRepository reactorRepository;

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

            Optional<Adsorbente> adsorbente = adsorbenteRepository.findByNombreAndAndParticulaT(nombreAdsorbente, sizeAdsorbente);

            Adsorbente nuevoAdsorbente = new Adsorbente();
            if (!adsorbente.isPresent()) {
                nuevoAdsorbente.setNombre(nombreAdsorbente);
                nuevoAdsorbente.setParticulaT(sizeAdsorbente);
                nuevoAdsorbente.setpHCargaCero(pHCargaCero);
                nuevoAdsorbente.setsBet(sBet);
                nuevoAdsorbente.setvBet(vBet);
                adsorbenteRepository.save(nuevoAdsorbente);

                nuevoSorbente = true;
            }

            Optional<Adsorbate> adsorbato = adsorbateRepository.findByNombreIonAndCargaIonAndRadioIonico(nombreIon, cargaIon, radioIonico);

            Adsorbate newAdsorbate = new Adsorbate();
            if (!adsorbato.isPresent()) {
                newAdsorbate.setNombreIon(nombreIon);
                newAdsorbate.setCargaIon(cargaIon);
                newAdsorbate.setRadioIonico(radioIonico);
                newAdsorbate.setLimiteVertido(limite);
                adsorbateRepository.save(newAdsorbate);

                nuevoSorbato = true;
            }

            Adsorbate sorbato = (nuevoSorbato ? newAdsorbate : adsorbato.get());
            Adsorbente sorbente = (nuevoSorbente ? nuevoAdsorbente : adsorbente.get());

            Optional<Reactor> reactor = reactorRepository.findByAdsorbenteAndAdsorbateAndQmaxAndTiempoEquilibrioAndTemperaturaAndPhinicial(sorbente, sorbato, qMax, tiempoEquilibrio, temperatura, pHInicial);

            if (!reactor.isPresent()) {
                Reactor nuevoReactor = new Reactor();
                nuevoReactor.setAdsorbente(sorbente);
                nuevoReactor.setAdsorbate(sorbato);
                nuevoReactor.setComplejacion(complejacionBool);
                nuevoReactor.setIntercambioIonico(intercambioBool);
                nuevoReactor.setReaccionQuimica(reaccionBool);
                nuevoReactor.setFuente(fuente);
                nuevoReactor.setObservacion(observaciones);
                nuevoReactor.setPhinicial(pHInicial);
                nuevoReactor.setQmax(qMax);
                nuevoReactor.setTemperatura(temperatura);
                nuevoReactor.setTiempoEquilibrio(tiempoEquilibrio);

                reactorRepository.save(nuevoReactor);
            }
        }
    }

    private boolean stringToBoolean(String string){
        return string.equals("si");
    }


}
