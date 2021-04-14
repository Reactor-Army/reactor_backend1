package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class AdsorbenteServiceTests {

    @Autowired
    private AdsorbenteService adsorbenteService;


    @Test
    public void testCreateAdsorbente(){
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(request);

        Assert.assertEquals(adsorbente.getNombre(), request.getNombre());
        Assert.assertEquals(adsorbente.getParticulaT(), request.getParticulaT());
        Assert.assertEquals(adsorbente.getvBet(), request.getvBet());
    }

    @Test
    public void testFindAll() {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(request);
        List<Adsorbente> adsorbentes = adsorbenteService.getAll();
        Assert.assertTrue(adsorbentes.size() == 1);
    }

    @Test
    public void testUpdateAdsorbente() throws ComponentNotFoundException {
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
    public void testComponentNotFoundExceptionUpdate() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbenteRequest requestUpdate = new AdsorbenteRequest("Prueba2", "Prueba2", 10f, 10f,10f);
            requestUpdate.setId(2L);
            Adsorbente updated = adsorbenteService.updateAdsorbente(requestUpdate);
        });
    }

    @Test
    public void testDeleteAdsorbente() throws ComponentNotFoundException {
        AdsorbenteRequest request = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbenteService.createAdsorbente(request);
        adsorbenteService.deleteAdsorbente(1L);
        Assert.assertTrue(adsorbenteService.getAll().isEmpty());

    }

    @Test
    public void testComponentNotFoundExceptionDelete() throws ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbenteService.deleteAdsorbente(2L);
        });
    }

}
