package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbenteResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@WithMockUser(username = "usertest", password = "password", roles = "ADMIN")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbenteControllerTest {

    @Autowired
    private AdsorbenteController adsorbenteController;

    @Test
    void testCreateAdsorbente(){
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(request);

        Assert.assertEquals(adsorbente.getNombre(), request.getNombre());
        Assert.assertEquals(adsorbente.getParticulaT(), request.getParticulaT());
        Assert.assertEquals(adsorbente.getvBet(), request.getvBet());
    }

    @Test
    void testCreateAdsorbenteSinNombre(){
        AdsorbenteRequest request = new AdsorbenteRequest(null, "Prueba", 1f, 1f,1f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(request);
        });
    }

    @Test
    void testGetAllAdsorbentes() {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente =adsorbenteController.createAdsorbente(request);
        List<AdsorbenteResponse> adsorbentes = adsorbenteController.getAdsorbentes();
        Assert.assertEquals(1L,adsorbentes.size());
    }

    @Test
    void testUpdateAdsorbente() {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(1L);
        adsorbenteController.createAdsorbente(request);
        AdsorbenteResponse updated = adsorbenteController.updateAdsorbente(requestUpdate);

        Assert.assertEquals(updated.getNombre(), requestUpdate.getNombre());
        Assert.assertEquals(updated.getParticulaT(), requestUpdate.getParticulaT());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testComponentNotFoundExceptionUpdate(){
        AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(2L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbenteController.updateAdsorbente(requestUpdate);
        });
    }

    @Test
    void testUpdateAdsorbenteSinID() {
        AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbenteController.updateAdsorbente(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbente() {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbenteController.createAdsorbente(request);
        adsorbenteController.deleteAdsorbente(1L);
        Assert.assertTrue(adsorbenteController.getAdsorbentes().isEmpty());
    }

    @Test
    void testDeleteAdsorbenteSinID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbenteController.deleteAdsorbente(1L);
        });
    }
}
