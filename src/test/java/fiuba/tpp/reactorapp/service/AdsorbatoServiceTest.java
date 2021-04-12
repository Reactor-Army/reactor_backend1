package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class AdsorbatoServiceTest {

    @Autowired
    private AdsorbatoService adsorbatoService;


    @Test
    public void createAdsorbatoTest(){
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba",1.2f,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(request);

        Assert.assertEquals(adsorbato.getNombreIon(), request.getNombreIon());
        Assert.assertEquals(adsorbato.getCargaIon(), request.getCargaIon());
        Assert.assertEquals(adsorbato.getRadioIonico(), request.getRadioIonico());
    }


    @Test
    public void findAllTest() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba",1.2f,1f,10f);
        adsorbatoService.createAdsorbato(request);
        List<Adsorbato> adsorbatos = adsorbatoService.getAll();
        Assert.assertTrue(adsorbatos.size() == 1);
    }


}
