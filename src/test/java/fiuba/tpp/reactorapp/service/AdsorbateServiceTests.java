package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateIUPACNameException;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbateServiceTests {

    @Autowired
    private AdsorbateService adsorbateService;


    @Test
    void testCreateAdsorbate() throws DuplicateIUPACNameException {
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
    void testCreateAdsorbateFormula(Integer charge, String formula, String formulaResult) throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",charge,1f,10f);
        request.setFormula(formula);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);
        Assert.assertEquals( formulaResult,adsorbate.getFormula());

    }

    @Test
    void testCreateAdsorbateNameIUPACNull() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba",null,1,1f,10f);
        Assertions.assertThrows(NullPointerException.class, () -> {
            adsorbateService.createAdsorbate(request);
        });
    }


    @Test
    void testFindAll() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateService.createAdsorbate(request);
        List<Adsorbate> adsorbates = adsorbateService.getAll();
        Assert.assertEquals(1L, adsorbates.size());
    }

    @Test
    void testUpdateAdsorbate() throws ComponentNotFoundException, DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        adsorbateService.createAdsorbate(request);
        Adsorbate updated = adsorbateService.updateAdsorbate(1l, requestUpdate);


        Assert.assertEquals(updated.getIonName(), requestUpdate.getIonName());
        Assert.assertEquals(updated.getIonCharge(), requestUpdate.getIonCharge());
        Assert.assertEquals(updated.getIonRadius(), requestUpdate.getIonRadius());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
            AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
            adsorbateService.createAdsorbate(request);
            Adsorbate updated = adsorbateService.updateAdsorbate(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbato() throws ComponentNotFoundException, DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.deleteAdsorbate(1l);
        Assert.assertTrue(adsorbateService.getAll().isEmpty());

    }

    @Test
    void testComponentNotFoundExceptionDelete() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
            adsorbateService.createAdsorbate(request);
            adsorbateService.deleteAdsorbate(2L);
        });
    }

    @Test
    void testSearchAdsorbatoNoFilter() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(null,null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbatoFilterIUPAC() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("IUPAC2",null));
        Assert.assertEquals(1L, adsorbates.size());
    }


    @ParameterizedTest
    @CsvSource({
            "1, 'IUPAC2'",
            "2, ",
            "2, ''"
    })
    void testSearchAdsorbateFilterIUPACAndCharge(long size, String filter) throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(filter,1));
        Assert.assertEquals(size, adsorbates.size());
    }


    @Test
    void testSearchAdsorbateFilterUpperAndLowerIUPAC() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("prueba",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLower() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent1() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("cárlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent2() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("prueba","cárlos",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("cárlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testDylan() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Anália","IUPAC2",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Analía","IUPAC",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("ANALIA",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testCreateAdsorbateDuplicateIUPACName() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbateService.createAdsorbate(request);

        Assertions.assertThrows(DuplicateIUPACNameException.class, () -> {
            adsorbateService.createAdsorbate(request);
        });
    }

    @Test
    void testUpdateAdsorbateDuplicateIUPACName() throws DuplicateIUPACNameException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba","PruebaIUPAC2",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC2",12,10f,100f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);

        Assertions.assertThrows(DuplicateIUPACNameException.class, () -> {
            adsorbateService.updateAdsorbate(1L, requestUpdate);
        });
    }

}
