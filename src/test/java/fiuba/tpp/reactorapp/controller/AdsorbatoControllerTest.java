package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbatoResponse;
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
class AdsorbatoControllerTest {

    @Autowired
    private AdsorbatoController adsorbatoController;

    @Test
    void testCreateAdsorbato(){
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);

        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(request);

        Assert.assertEquals(adsorbato.getNombreIon(), request.getNombreIon());
        Assert.assertEquals(adsorbato.getCargaIon(), request.getCargaIon());
        Assert.assertEquals(adsorbato.getRadioIonico(), request.getRadioIonico());
        Assert.assertEquals(1L, (long) adsorbato.getId());
    }

    @Test
    void testCreateAdsorbatoSinNombre(){
        AdsorbatoRequest request = new AdsorbatoRequest(null,"PruebaIUPAC",1,1f,10f);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(request);
        });
    }

    @Test
    void testUpdateAdsorbato(){
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest requestUpdate = new AdsorbatoRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        requestUpdate.setId(1L);
        adsorbatoController.createAdsorbato(request);
        AdsorbatoResponse updated = adsorbatoController.updateAdsorbato(requestUpdate);

        Assert.assertEquals(updated.getNombreIon(), requestUpdate.getNombreIon());
        Assert.assertEquals(updated.getCargaIon(), requestUpdate.getCargaIon());
        Assert.assertEquals(updated.getRadioIonico(), requestUpdate.getRadioIonico());
    }

    @Test
    void testUpdateAdsorbatoNoExiste() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest requestUpdate = new AdsorbatoRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        requestUpdate.setId(2L);
        adsorbatoController.createAdsorbato(request);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbatoController.updateAdsorbato(requestUpdate);
        });
    }

    @Test
    void testUpdateAdsorbatoSinID() {
        AdsorbatoRequest requestUpdate = new AdsorbatoRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbatoController.updateAdsorbato(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbato() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.deleteAdsorbato(1L);
        Assert.assertTrue(adsorbatoController.getAdsorbatos().isEmpty());
    }

    @Test
    void testDeleteAdsorbatoSinID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbatoController.deleteAdsorbato(1L);
        });
    }

    @Test
    void testGetAllAdsorbatos() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.getAdsorbatos();
        Assert.assertEquals(1L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoNoFilter() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos(null,null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoFilterIUPAC() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos("IUPAC2",null);
        Assert.assertEquals(1L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoFilterIUPACAndCarga() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos("IUPAC2",1);
        Assert.assertEquals(1L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoFilterdCarga() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos(null,1);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoFilterUpperAndLowerIUPAC() {
        AdsorbatoRequest request = new AdsorbatoRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("carlos","prueba",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos("PRUEBA",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatoFilterUpperAndLower() {
        AdsorbatoRequest request = new AdsorbatoRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("carlos","prueba",1,1f,10f);
        adsorbatoController.createAdsorbato(request);
        adsorbatoController.createAdsorbato(request2);
        List<AdsorbatoResponse> adsorbatos = adsorbatoController.searchAdsorbatos("CARLOS",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }


}
