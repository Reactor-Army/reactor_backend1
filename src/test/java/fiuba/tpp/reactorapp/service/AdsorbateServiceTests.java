package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
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
    void testCreateAdsorbate(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);

        Assert.assertEquals(adsorbate.getIonName(), request.getIonName());
        Assert.assertEquals(adsorbate.getIonCharge(), request.getIonCharge());
        Assert.assertEquals(adsorbate.getIonRadius(), request.getIonRadius());
    }

    @Test
    void testCreateAdsorbateNameIUPACNull(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba",null,1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(request);

        Assert.assertEquals(adsorbate.getIonName(), request.getIonName());
        Assert.assertEquals(adsorbate.getIonCharge(), request.getIonCharge());
        Assert.assertEquals(adsorbate.getIonRadius(), request.getIonRadius());
    }


    @Test
    void testFindAll() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateService.createAdsorbate(request);
        List<Adsorbate> adsorbates = adsorbateService.getAll();
        Assert.assertEquals(1L, adsorbates.size());
    }

    @Test
    void testUpdateAdsorbate() throws ComponentNotFoundException {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        requestUpdate.setId(1L);
        adsorbateService.createAdsorbate(request);
        Adsorbate updated = adsorbateService.updateAdsorbate(requestUpdate);


        Assert.assertEquals(updated.getIonName(), requestUpdate.getIonName());
        Assert.assertEquals(updated.getIonCharge(), requestUpdate.getIonCharge());
        Assert.assertEquals(updated.getIonRadius(), requestUpdate.getIonRadius());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
            AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
            requestUpdate.setId(2L);
            adsorbateService.createAdsorbate(request);
            Adsorbate updated = adsorbateService.updateAdsorbate(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbato() throws ComponentNotFoundException {
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
    void testSearchAdsorbatoNoFilter() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(null,null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbatoFilterIUPAC() {
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
    void testSearchAdsorbateFilterIUPACAndCharge(long size, String filter) {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter(filter,1));
        Assert.assertEquals(size, adsorbates.size());
    }


    @Test
    void testSearchAdsorbateFilterUpperAndLowerIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("prueba",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLower() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent1() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("cárlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterAccent2() {
        AdsorbateRequest request = new AdsorbateRequest("prueba","cárlos",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("cárlos","prueba",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("carlos",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

    @Test
    void testDylan() {
        AdsorbateRequest request = new AdsorbateRequest("Anália","",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Analía","",1,1f,10f);
        adsorbateService.createAdsorbate(request);
        adsorbateService.createAdsorbate(request2);
        List<Adsorbate> adsorbates = adsorbateService.search(new AdsorbateFilter("ANALIA",null));
        Assert.assertEquals(2L, adsorbates.size());
    }

}
