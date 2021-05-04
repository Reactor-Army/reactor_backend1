package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ProcessResponse;
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
class ProcessControllerTest {

    @Autowired
    private ProcessController processController;

    @Autowired
    private AdsorbentController adsorbentController;

    @Autowired
    private AdsorbateController adsorbateController;


    @Test
    void testCreateProcessController() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        Assert.assertEquals(process.getAdsorbato().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbente().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), request.getQmax());
    }

    @Test
    void testCreateInvalidProcess(){
        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(1L);
        request.setIdAdsorbente(1L);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.createProcess(request);
        });
    }


    @Test
    void testGetProcesses(){
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbente = adsorbentController.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(requestAdsorbato);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbato.getId());
        request.setIdAdsorbente(adsorbente.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.getProcesses();
        Assert.assertEquals(1L,processes.size());
    }

    @Test
    void testUpdateProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbate.getId());
        requestUpdate.setIdAdsorbente(adsorbent.getId());
        requestUpdate.setId(1L);

        ProcessResponse process = processController.updateProcess(requestUpdate);

        Assert.assertEquals(process.getAdsorbato().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbente().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidProcessExceptionUpdate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbato(adsorbate.getId());
        requestUpdate.setIdAdsorbente(adsorbent.getId());
        requestUpdate.setId(2L);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.updateProcess(requestUpdate);
        });
    }

    @Test
    void testDeleteProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);
        processController.deleteProcess(1L);
        Assert.assertTrue(processController.getProcesses().isEmpty());
    }

    @Test
    void testProcessNotFoundExceptionDelete(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.deleteProcess(1L);
        });
    }

    @Test
    void testSearchProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.searchProcesses(1L,1L);

        Assert.assertEquals(1L, processes.size());
    }

    @Test
    void testSearchProcessAdsorbate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.searchProcesses(1L,null);

        Assert.assertEquals(1L,processes.size());
    }

    @Test
    void testSearchProcessAdsorbent(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbato(adsorbate.getId());
        request.setIdAdsorbente(adsorbent.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.searchProcesses(null,1L);

        Assert.assertEquals(1L,processes.size());
    }



}
