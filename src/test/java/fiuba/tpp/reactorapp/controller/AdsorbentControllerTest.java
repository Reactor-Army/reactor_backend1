package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentNameResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
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
class AdsorbentControllerTest {

    @Autowired
    private AdsorbentController adsorbentController;

    @Test
    void testCreateAdsorbent(){
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(request);

        Assert.assertEquals(adsorbent.getName(), request.getName());
        Assert.assertEquals(adsorbent.getParticleSize(), request.getParticleSize());
        Assert.assertEquals(adsorbent.getvBet(), request.getvBet());
    }

    @Test
    void testCreateAdsorbentWithoutName(){
        AdsorbentRequest request = new AdsorbentRequest(null, "Prueba", 1f, 1f,1f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentController.createAdsorbent(request);
        });
    }

    @Test
    void testGetAllAdsorbents() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        List<AdsorbentResponse> adsorbents = adsorbentController.getAdsorbents();
        Assert.assertEquals(1L,adsorbents.size());
    }

    @Test
    void testUpdateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(1L);
        adsorbentController.createAdsorbent(request);
        AdsorbentResponse updated = adsorbentController.updateAdsorbent(requestUpdate);

        Assert.assertEquals(updated.getName(), requestUpdate.getName());
        Assert.assertEquals(updated.getParticleSize(), requestUpdate.getParticleSize());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testComponentNotFoundExceptionUpdate(){
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(2L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentController.updateAdsorbent(requestUpdate);
        });
    }

    @Test
    void testUpdateAdsorbentWithoutID() {
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentController.updateAdsorbent(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.deleteAdsorbent(1L);
        Assert.assertTrue(adsorbentController.getAdsorbents().isEmpty());
    }

    @Test
    void testDeleteAdsorbentWithoutID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentController.deleteAdsorbent(1L);
        });
    }


    @Test
    void testSearchAdsorbentsNoFilter() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbentes = adsorbentController.searchAdsorbents(null);
        Assert.assertEquals(2L,adsorbentes.size());
    }

    @Test
    void testSearchAdsorbentsFilterName() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbents = adsorbentController.searchAdsorbents("Prueba2");
        Assert.assertEquals(1L,adsorbents.size());
    }

    @Test
    void testSearchAdsorbentFilterUpperAndLowerName() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbatos = adsorbentController.searchAdsorbents("PRUEBA");
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbentName() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentNameResponse> adsorbentsName = adsorbentController.searchAdsorbentsName("PRUEBA");
        Assert.assertEquals(2L,adsorbentsName.size());
        Assert.assertEquals("PRUEBA (Prueba)", adsorbentsName.get(0).getName());
        Assert.assertEquals("Prueba (Prueba2)", adsorbentsName.get(1).getName());
    }

    @Test
    void testSearchAdsorbentNameOneMatch() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
            AdsorbentRequest request2 = new AdsorbentRequest("EsteNoEs", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentNameResponse> adsorbentsName = adsorbentController.searchAdsorbentsName("PRUEBA");
        Assert.assertEquals(1L,adsorbentsName.size());
        Assert.assertEquals("PRUEBA (Prueba)", adsorbentsName.get(0).getName());

    }
}
