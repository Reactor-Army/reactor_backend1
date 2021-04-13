package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import org.junit.Assert;

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
}
