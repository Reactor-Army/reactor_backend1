package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateIUPACNameException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

@SpringBootTest
class AdsorbateServiceTests {

    @Autowired
    private AdsorbateService adsorbateService;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @AfterEach
    void resetDatabase(){
        adsorbateRepository.deleteAll();
    }


    @Test
    void testCreateAdsorbate() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);

        Assert.assertEquals(adsorbate.getIonName(), request.getIonName());
        Assert.assertEquals(adsorbate.getIonCharge(), request.getIonCharge());
        Assert.assertEquals(adsorbate.getIonRadius(), request.getIonRadius());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'H2O+','H2O'",
            "1, 'H2O1+','H2O'",
            "-1, 'H2O1-','H2O'",
            "-1, 'H2O-','H2O'",
            "-2, 'H2O2-','H2O'",
            "2, '', ''",
            "2, ,",
            "2, 'H2O42+', 'H2O4'",

    })
    void testCreateAdsorbateFormula(Integer charge, String formula, String formulaResult) {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",charge,1f,10f);
        request.setFormula(formula);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);
        Assert.assertEquals( formulaResult,adsorbate.getFormula());

    }

    @Test
    void testCreateAdsorbateNameIUPACNull() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba",null,1,1f,10f);
        Assertions.assertThrows(NullPointerException.class, () -> {
            adsorbateService.createAdsorbate(request);
        });
    }


    @Test
    void testFindAll() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateService.createAdsorbate(request);
        List<Adsorbate> adsorbates = adsorbateService.getAdsorbates(false);
        Assert.assertEquals(1L, adsorbates.size());
    }

    @Test
    void testUpdateAdsorbate() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);
        Adsorbate updated = adsorbateService.updateAdsorbate(adsorbate.getId(), requestUpdate);

        Assert.assertEquals(updated.getIonName(), requestUpdate.getIonName());
        Assert.assertEquals(updated.getIonCharge(), requestUpdate.getIonCharge());
        Assert.assertEquals(updated.getIonRadius(), requestUpdate.getIonRadius());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        adsorbateService.createAdsorbate(request);
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbateService.updateAdsorbate(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbato() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);
        adsorbateService.deleteAdsorbate(adsorbate.getId());
        Assert.assertTrue(adsorbateService.getAdsorbates(false).isEmpty());

    }

    @Test
    void testComponentNotFoundExceptionDelete() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbateService.deleteAdsorbate(2L);
        });
    }

    @Test
    void testSearchAdsorbatoNoFilter() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(null, (Integer) null),false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbatoFilterIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("IUPAC2", (Integer) null),false);
        Assert.assertEquals(1L, adsorbates.size());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 'IUPAC2'",
            "2, ",
            "2, ''"
    })
    void testSearchAdsorbateFilterIUPACAndCharge(long size, String filter) {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(filter,1), false);
        Assert.assertEquals(size, adsorbates.size());
    }


    @Test
    void testSearchAdsorbateFilterUpperAndLowerIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("prueba", (Integer) null), false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLower() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos", (Integer) null), false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent1() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("c??rlos", (Integer) null), false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent2() {
        AdsorbateRequest request = new AdsorbateRequest("prueba","c??rlos",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("c??rlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos", (Integer) null), false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testDylan() {
        AdsorbateRequest request = new AdsorbateRequest("An??lia","IUPAC2",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Anal??a","IUPAC",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("ANALIA", (Integer) null), false);
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testCreateAdsorbateDuplicateIUPACName() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbateService.createAdsorbate(request);

        Assertions.assertThrows(DuplicateIUPACNameException.class, () -> {
            adsorbateService.createAdsorbate(request);
        });
    }

    @Test
    void testUpdateAdsorbateDuplicateIUPACName() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba","PruebaIUPAC2",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC2",12,10f,100f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);

        Long adsorbateId = adsorbate.getId();

        Assertions.assertThrows(DuplicateIUPACNameException.class, () -> {
            adsorbateService.updateAdsorbate(adsorbateId, requestUpdate);
        });
    }

    @Test
    void testSearchIdAdsorbent() {
        AdsorbateRequest request = new AdsorbateRequest("prueba","c??rlos",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("c??rlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos", 1L), false);
        Assert.assertEquals(0L, adsorbates.size());
    }

}
