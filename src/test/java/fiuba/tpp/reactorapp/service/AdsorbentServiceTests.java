package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbentServiceTests {

    @Autowired
    private AdsorbentService adsorbentService;


    @Test
    void testCreateAdsorbent(){
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);

        Assert.assertEquals(adsorbent.getName(), request.getNombre());
        Assert.assertEquals(adsorbent.getParticleSize(), request.getParticulaT());
        Assert.assertEquals(adsorbent.getvBet(), request.getvBet());
    }

    @Test
    void testFindAll() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);
        List<Adsorbent> adsorbents = adsorbentService.getAll();
        Assert.assertEquals(1L, adsorbents.size());
    }

    @Test
    void testUpdateAdsorbent() throws ComponentNotFoundException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(1L);
        adsorbentService.createAdsorbent(request);
        Adsorbent updated = adsorbentService.updateAdsorbent(requestUpdate);

        Assert.assertEquals(updated.getName(), requestUpdate.getNombre());
        Assert.assertEquals(updated.getParticleSize(), requestUpdate.getParticulaT());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
            requestUpdate.setId(2L);
            Adsorbent updated = adsorbentService.updateAdsorbent(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbent() throws ComponentNotFoundException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.deleteAdsorbent(1L);
        Assert.assertTrue(adsorbentService.getAll().isEmpty());

    }

    @Test
    void testComponentNotFoundExceptionDelete() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbentService.deleteAdsorbent(2L);
        });
    }

}
