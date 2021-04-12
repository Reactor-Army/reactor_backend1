package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.repository.AdsorbatoRepository;
import fiuba.tpp.reactorapp.repository.AdsorbenteRepository;
import fiuba.tpp.reactorapp.repository.ReactorRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class LoadDataService {

    @Autowired
    private AdsorbenteRepository adsorbenteRepository;

    @Autowired
    private AdsorbatoRepository adsorbatoRepository;

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
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=3;i<worksheet.getPhysicalNumberOfRows() ;i++) {
                boolean nuevoSorbente= false;
                boolean nuevoSorbato = false;


                XSSFRow row = worksheet.getRow(i);

                if(row.getCell(0) != null){
                    String nombreAdsorbente = row.getCell(0).getStringCellValue();
                    String sizeAdsorbente = row.getCell(1).getStringCellValue();
                    String nombreIon = row.getCell(2).getStringCellValue();

                    String cargaIonText = row.getCell(3).getStringCellValue();
                    StringBuilder cargaBuilder = new StringBuilder();
                    cargaBuilder.append(cargaIonText);
                    cargaBuilder.reverse();
                    Float cargaIon = Float.valueOf(cargaBuilder.toString());

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
                    if(!adsorbente.isPresent()){
                        nuevoAdsorbente.setNombre(nombreAdsorbente);
                        nuevoAdsorbente.setParticulaT(sizeAdsorbente);
                        nuevoAdsorbente.setpHCargaCero(pHCargaCero);
                        nuevoAdsorbente.setsBet(sBet);
                        nuevoAdsorbente.setvBet(vBet);
                        adsorbenteRepository.save(nuevoAdsorbente);

                        nuevoSorbente = true;
                    }

                    Optional<Adsorbato> adsorbato = adsorbatoRepository.findByNombreIonAndCargaIonAndRadioIonico(nombreIon,cargaIon,radioIonico);

                    Adsorbato nuevoAdsorbato = new Adsorbato();
                    if(!adsorbato.isPresent()){
                        nuevoAdsorbato.setNombreIon(nombreIon);
                        nuevoAdsorbato.setCargaIon(cargaIon);
                        nuevoAdsorbato.setRadioIonico(radioIonico);
                        nuevoAdsorbato.setLimiteVertido(limite);
                        adsorbatoRepository.save(nuevoAdsorbato);

                        nuevoSorbato = true;
                    }

                    Adsorbato sorbato = (nuevoSorbato? nuevoAdsorbato: adsorbato.get());
                    Adsorbente sorbente = (nuevoSorbente? nuevoAdsorbente: adsorbente.get());

                    Optional<Reactor> reactor = reactorRepository.findByAdsorbenteAndAdsorbatoAndQmaxAndTiempoEquilibrioAndTemperaturaAndPhinicial(sorbente,sorbato,qMax,tiempoEquilibrio,temperatura,pHInicial);

                    if(!reactor.isPresent()){
                        Reactor nuevoReactor = new Reactor();
                        nuevoReactor.setAdsorbente(sorbente);
                        nuevoReactor.setAdsorbato(sorbato);
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

            ArrayList<Adsorbente> adsorbentes = (ArrayList<Adsorbente>) adsorbenteRepository.findAll();

            for (Adsorbente ads:adsorbentes) {
                System.out.println(ads.toString());
            }

            ArrayList<Adsorbato> adsorbatos = (ArrayList<Adsorbato>) adsorbatoRepository.findAll();

            for (Adsorbato adsor:adsorbatos) {
                System.out.println(adsor.toString());
            }

            ArrayList<Reactor> reactores = (ArrayList<Reactor>) reactorRepository.findAll();

            for (Reactor reac:reactores) {
                System.out.println(reac.toString());
            }
            System.out.println(reactores.size());


        }

    }

    private boolean stringToBoolean(String string){
        if( string.equals("si")){
            return true;
        }
        return false;
    }


}
