package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidReactorException;
import fiuba.tpp.reactorapp.model.filter.ReactorFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbatoRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbenteRequest;
import fiuba.tpp.reactorapp.model.request.ReactorRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbatoResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbenteResponse;
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
    private AdsorbenteController adsorbenteController;

    @Autowired
    private AdsorbatoController adsorbatoController;


    @Test
    void testCreateReactorController() {
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        ReactorResponse reactor = reactorController.createReactor(request);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbato.getId());
        Assert.assertEquals(reactor.getAdsorbente().getId(), adsorbente.getId());
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
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.getReactores();
        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testUpdateReactor(){
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbato.getId());
        requestUpdate.setIdAdsorbente(adsorbente.getId());
        requestUpdate.setId(1L);

        ReactorResponse reactor = reactorController.updateReactor(requestUpdate);

        Assert.assertEquals(reactor.getAdsorbato().getId(), adsorbato.getId());
        Assert.assertEquals(reactor.getAdsorbente().getId(), adsorbente.getId());
        Assert.assertEquals(reactor.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidReactorExceptionUpdate(){
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        ReactorRequest requestUpdate = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbato.getId());
        requestUpdate.setIdAdsorbente(adsorbente.getId());
        requestUpdate.setId(2L);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            reactorController.updateReactor(requestUpdate);
        });
    }

    @Test
    void testDeleteReactor(){
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
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
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(1L,1L);

        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testSearchReactorAdsorbato(){
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(1L,null);

        Assert.assertEquals(1L,reactores.size());
    }

    @Test
    void testSearchReactorAdsorbente(){
        AdsorbenteRequest requestAdsorbente = new AdsorbenteRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbenteResponse adsorbente = adsorbenteController.createAdsorbente(requestAdsorbente);

        AdsorbatoRequest requestAdsorbato = new AdsorbatoRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbatoResponse adsorbato = adsorbatoController.createAdsorbato(requestAdsorbato);

        ReactorRequest request = new ReactorRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        reactorController.createReactor(request);

        List<ReactorResponse> reactores = reactorController.searchReactores(null,1L);

        Assert.assertEquals(1L,reactores.size());
    }



}
