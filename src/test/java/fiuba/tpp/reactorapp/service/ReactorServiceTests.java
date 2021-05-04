package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.filter.ReactorFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReactorServiceTests {

    @Autowired
    private ReactorService reactorService;

    @Autowired
    private AdsorbentService adsorbentService;

    @Autowired
    private AdsorbateService adsorbateService;


    @Test
    void testCreateReactor() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        Reactor reactor = reactorService.createReactor(request);

        Assert.assertEquals(reactor.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(reactor.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(reactor.getQmax(), request.getQmax());
    }

    @Test
    void testInvalidReactorExceptionCreate(){
        Assertions.assertThrows(InvalidReactorException.class, () -> {
            ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
            request.setIdAdsorbato(1L);
            request.setIdAdsorbente(1L);
            reactorService.createReactor(request);
        });
    }


    @Test
    void testFindAll() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        Reactor reactor = reactorService.createReactor(request);

        List<Reactor> reactores = reactorService.getAll();
        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testUpdateReactor() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorService.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbate.getId());
        requestUpdate.setIdAdsorbente(adsorbent.getId());
        requestUpdate.setId(1L);

        Reactor reactor = reactorService.updateReactor(requestUpdate);

        Assert.assertEquals(reactor.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(reactor.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(reactor.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidReactorExceptionUpdate() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorService.createReactor(request);

        Assertions.assertThrows(InvalidReactorException.class, () -> {

            ReactorRequest requestUpdate = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
            requestUpdate.setIdAdsorbato(adsorbate.getId());
            requestUpdate.setIdAdsorbente(adsorbent.getId());
            requestUpdate.setId(2L);
            reactorService.updateReactor(requestUpdate);
        });
    }

    @Test
    void testDeleteReactor() throws InvalidReactorException, ComponentNotFoundException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorService.createReactor(request);
        reactorService.deleteReactor(1L);
        Assert.assertTrue(reactorService.getAll().isEmpty());
    }

    @Test
    void testReactorNotFoundExceptionDelete() throws InvalidReactorException, ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            reactorService.deleteReactor(1L);
        });
    }

    @Test
    void testSearchReactor() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        AdsorbateRequest requestAdsorbato2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        Adsorbate adsorbate2 = adsorbateService.createAdsorbate(requestAdsorbato2);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorService.createReactor(request);

        request.setIdAdsorbato(adsorbate2.getId());
        reactorService.createReactor(request);


        List<Reactor> reactores = reactorService.search(new ReactorFilter(null, 1L));
        Assert.assertEquals(2L, reactores.size());

    }

    @Test
    void testSearchReactorAdsorbatoAdsorbente() throws InvalidReactorException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        AdsorbateRequest requestAdsorbato2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        Adsorbate adsorbate2 = adsorbateService.createAdsorbate(requestAdsorbato2);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorService.createReactor(request);

        request.setIdAdsorbato(adsorbate2.getId());
        reactorService.createReactor(request);


        List<Reactor> reactores = reactorService.search(new ReactorFilter(1L, 1L));
        Assert.assertEquals(1L, reactores.size());

    }


}
