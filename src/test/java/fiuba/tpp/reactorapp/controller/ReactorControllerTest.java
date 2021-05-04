package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ReactorResponse;
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
class ReactorControllerTest {

    @Autowired
    private ReactorController reactorController;

    @Autowired
    private AdsorbentController adsorbentController;

    @Autowired
    private AdsorbateController adsorbateController;


    @Test
    void testCreateReactorController() {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbente = adsorbentController.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        ReactorResponse reactor = reactorController.createReactor(request);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbato.getId());
        Assert.assertEquals(reactor.getAdsorbent().getId(), adsorbente.getId());
        Assert.assertEquals(reactor.getQmax(), request.getQmax());
    }

    @Test
    void testCreateInvalidReactor(){
        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(1L);
        request.setIdAdsorbente(1L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            reactorController.createReactor(request);
        });
    }


    @Test
    void testGetReactors(){
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbente = adsorbentController.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.getReactores();
        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testUpdateReactor(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbate.getId());
        requestUpdate.setIdAdsorbente(adsorbent.getId());
        requestUpdate.setId(1L);

        ReactorResponse reactor = reactorController.updateReactor(requestUpdate);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbate.getId());
        Assert.assertEquals(reactor.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(reactor.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidReactorExceptionUpdate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbate.getId());
        requestUpdate.setIdAdsorbente(adsorbent.getId());
        requestUpdate.setId(2L);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            reactorController.updateReactor(requestUpdate);
        });
    }

    @Test
    void testDeleteReactor(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);
        reactorController.deleteReactor(1L);
        Assert.assertTrue(reactorController.getReactores().isEmpty());
    }

    @Test
    void testReactorNotFoundExceptionDelete(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            reactorController.deleteReactor(1L);
        });
    }

    @Test
    void testSearchReactor(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(1L,1L);

        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testSearchReactorAdsorbato(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(1L,null);

        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testSearchReactorAdsorbente(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(null,1L);

        Assert.assertEquals(1L,reactores.size());
    }



}
