package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbenteServiceTests {

    @Autowired
    private AdsorbenteService adsorbenteService;


    @Test
    void testCreateAdsorbente(){
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(request);

        Assert.assertEquals(adsorbente.getNombre(), request.getNombre());
        Assert.assertEquals(adsorbente.getParticulaT(), request.getParticulaT());
        Assert.assertEquals(adsorbente.getvBet(), request.getvBet());
    }

    @Test
    void testFindAll() {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(request);
        List<Adsorbente> adsorbentes = adsorbenteService.getAll();
        Assert.assertEquals(1L,adsorbentes.size());
    }

    @Test
    void testUpdateAdsorbente() throws ComponentNotFoundException {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(1L);
        adsorbenteService.createAdsorbente(request);
        Adsorbente updated = adsorbenteService.updateAdsorbente(requestUpdate);

        Assert.assertEquals(updated.getNombre(), requestUpdate.getNombre());
        Assert.assertEquals(updated.getParticulaT(), requestUpdate.getParticulaT());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
            requestUpdate.setId(2L);
            Adsorbente updated = adsorbenteService.updateAdsorbente(requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbente() throws ComponentNotFoundException {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbenteService.createAdsorbente(request);
        adsorbenteService.deleteAdsorbente(1L);
        Assert.assertTrue(adsorbenteService.getAll().isEmpty());

    }

    @Test
    void testComponentNotFoundExceptionDelete() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbenteService.deleteAdsorbente(2L);
        });
    }

}
