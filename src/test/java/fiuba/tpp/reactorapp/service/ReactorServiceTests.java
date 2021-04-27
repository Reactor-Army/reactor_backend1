package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
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
public class ReactorServiceTests {

    @Autowired
    private ReactorService reactorService;

    @Autowired
    private AdsorbenteService adsorbenteService;

    @Autowired
    private AdsorbatoService adsorbatoService;


    @Test
    public void testCreateReactor() throws InvalidReactorException {
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1.2f,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
        Reactor reactor = reactorService.createReactor(request);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbato.getId());
        Assert.assertEquals(reactor.getAdsorbente().getId(), adsorbente.getId());
        Assert.assertEquals(reactor.getQmax(), request.getQmax());
    }

    @Test
    public void testInvalidReactorExceptionCreate(){
        Assertions.assertThrows(InvalidReactorException.class, () -> {
            ReactorRequest request = new ReactorRequest(1L,1L,0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
            reactorService.createReactor(request);
        });
    }


    @Test
    public void testFindAll() throws InvalidReactorException {
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1.2f,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
        Reactor reactor = reactorService.createReactor(request);

        List<Reactor> reactores = reactorService.getAll();
        Assert.assertTrue(reactores.size() == 1);
    }

    @Test
    public void testUpdateReactor() throws InvalidReactorException {
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1.2f,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
        reactorService.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
        requestUpdate.setId(1L);

        Reactor reactor = reactorService.updateReactor(requestUpdate);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbato.getId());
        Assert.assertEquals(reactor.getAdsorbente().getId(), adsorbente.getId());
        Assert.assertEquals(reactor.getQmax(), requestUpdate.getQmax());
    }

    @Test
    public void testInvalidReactorExceptionUpdate(){
        Assertions.assertThrows(InvalidReactorException.class, () -> {
            AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
            Adsorbente adsorbente = adsorbenteService.createAdsorbente(requestAdsorbente);

            AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1.2f,1f,10f);
            Adsorbato adsorbato = adsorbatoService.createAdsorbato(requestAdsorbato);

            ReactorRequest request = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
            reactorService.createReactor(request);

            ReactorRequest requestUpdate = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
            requestUpdate.setId(2L);
            reactorService.updateReactor(requestUpdate);
        });
    }

    @Test
    public void testDeleteReactor() throws InvalidReactorException, ComponentNotFoundException {
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbente adsorbente = adsorbenteService.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1.2f,1f,10f);
        Adsorbato adsorbato = adsorbatoService.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(adsorbato.getId(),adsorbente.getId(),0.65f,1f,1f,1f,true,true,true,"Prueba", "Prueba");
        reactorService.createReactor(request);
        reactorService.deleteReactor(1L);
        Assert.assertTrue(reactorService.getAll().isEmpty());
    }

    @Test
    public void testReactorNotFoundExceptionDelete() throws InvalidReactorException, ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            reactorService.deleteReactor(1L);
        });
    }

}
