package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbateControllerTest {

    @Autowired
    private AdsorbateController adsorbateController;

    @Test
    void testCreateAdsorbate(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);

        AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(request);

        Assert.assertEquals(adsorbato.getNombreIon(), request.getNombreIon());
        Assert.assertEquals(adsorbato.getCargaIon(), request.getCargaIon());
        Assert.assertEquals(adsorbato.getRadioIonico(), request.getRadioIonico());
        Assert.assertEquals(1L, (long) adsorbato.getId());
    }

    @Test
    void testCreateAdsorbateWithoutName(){
        AdsorbateRequest request = new AdsorbateRequest(null,"PruebaIUPAC",1,1f,10f);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(request);
        });
    }

    @Test
    void testUpdateAdsorbate(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        requestUpdate.setId(1L);
        adsorbateController.createAdsorbate(request);
        AdsorbateResponse updated = adsorbateController.updateAdsorbate(requestUpdate);

        Assert.assertEquals(updated.getNombreIon(), requestUpdate.getNombreIon());
        Assert.assertEquals(updated.getCargaIon(), requestUpdate.getCargaIon());
        Assert.assertEquals(updated.getRadioIonico(), requestUpdate.getRadioIonico());
    }

    @Test
    void testUpdateAdsorbatoThatNotExist() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        requestUpdate.setId(2L);
        adsorbateController.createAdsorbate(request);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.updateAdsorbate(requestUpdate);
        });
    }

    @Test
    void testUpdateAdsorbateWithoutID() {
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.updateAdsorbate(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbate() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.deleteAdsorbate(1L);
        Assert.assertTrue(adsorbateController.getAdsorbates().isEmpty());
    }

    @Test
    void testDeleteAdsorbatoWithoutID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.deleteAdsorbate(1L);
        });
    }

    @Test
    void testGetAllAdsorbates() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateController.createAdsorbate(request);
        List<AdsorbateResponse> adsorbates = adsorbateController.getAdsorbates();
        Assert.assertEquals(1L,adsorbates.size());
    }

    @Test
    void testSearchAdsorbatesNoFilter() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates(null,null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatesFilterIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbates = adsorbateController.searchAdsorbates("IUPAC2",null);
        Assert.assertEquals(1L,adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterIUPACAndCarga() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("IUPAC2",1);
        Assert.assertEquals(1L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterdCarga() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates(null,1);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLowerIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("PRUEBA",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLower() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("CARLOS",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }


}
