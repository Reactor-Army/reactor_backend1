package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdsorbatoServiceTests {

    @Autowired
    private AdsorbatoService adsorbatoService;


    @Test
    public void testCreateAdsorbato(){
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(request);

        Assert.assertEquals(adsorbato.getNombreIon(), request.getNombreIon());
        Assert.assertEquals(adsorbato.getCargaIon(), request.getCargaIon());
        Assert.assertEquals(adsorbato.getRadioIonico(), request.getRadioIonico());
    }


    @Test
    public void testFindAll() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        List<Adsorbato> adsorbatos = adsorbatoService.getAll();
        Assert.assertTrue(adsorbatos.size() == 1);
    }

    @Test
    public void testUpdateAdsorbato() throws ComponentNotFoundException {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest requestUpdate = new AdsorbatoRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        requestUpdate.setId(1L);
        adsorbatoService.createAdsorbato(request);
        Adsorbato updated = adsorbatoService.updateAdsorbato(requestUpdate);


        Assert.assertEquals(updated.getNombreIon(), requestUpdate.getNombreIon());
        Assert.assertEquals(updated.getCargaIon(), requestUpdate.getCargaIon());
        Assert.assertEquals(updated.getRadioIonico(), requestUpdate.getRadioIonico());
    }

    @Test
    public void testComponentNotFoundExceptionUpdate() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
            AdsorbatoRequest requestUpdate = new AdsorbatoRequest("Prueba2","PruebaIUPAC",1,10f,100f);
            requestUpdate.setId(2L);
            adsorbatoService.createAdsorbato(request);
            Adsorbato updated = adsorbatoService.updateAdsorbato(requestUpdate);
        });
    }

    @Test
    public void testDeleteAdsorbato() throws ComponentNotFoundException {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        adsorbatoService.deleteAdsorbato(1l);
        Assert.assertTrue(adsorbatoService.getAll().isEmpty());

    }

    @Test
    public void testComponentNotFoundExceptionDelete() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
            adsorbatoService.createAdsorbato(request);
            adsorbatoService.deleteAdsorbato(2L);
        });
    }

    @Test
    public void testSearchAdsorbatoNoFilter() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        adsorbatoService.createAdsorbato(request2);
        List<Adsorbato> adsorbatos = adsorbatoService.search(new AdsorbatoFilter(null,null));
        Assert.assertTrue(adsorbatos.size() == 2);
    }

    @Test
    public void testSearchAdsorbatoFilterIUPAC() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        adsorbatoService.createAdsorbato(request2);
        List<Adsorbato> adsorbatos = adsorbatoService.search(new AdsorbatoFilter("IUPAC2",null));
        Assert.assertTrue(adsorbatos.size() == 1);
    }

    @Test
    public void testSearchAdsorbatoFilterIUPACAndCarga() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        adsorbatoService.createAdsorbato(request2);
        List<Adsorbato> adsorbatos = adsorbatoService.search(new AdsorbatoFilter("IUPAC2",1));
        Assert.assertTrue(adsorbatos.size() == 1);
    }

    @Test
    public void testSearchAdsorbatoFilterdCarga() {
        AdsorbatoRequest request = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoRequest request2 = new AdsorbatoRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbatoService.createAdsorbato(request);
        adsorbatoService.createAdsorbato(request2);
        List<Adsorbato> adsorbatos = adsorbatoService.search(new AdsorbatoFilter(null,1));
        Assert.assertTrue(adsorbatos.size() == 2);
    }

}
