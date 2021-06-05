package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.SearchByAdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ProcessResponse;
import fiuba.tpp.reactorapp.model.response.SearchByAdsorbateResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), request.getQmax());
    }

    @Test
    void testCreateInvalidProcess(){
        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(1L);
        request.setIdAdsorbent(1L);
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
        request.setIdAdsorbate(adsorbato.getId());
        request.setIdAdsorbent(adsorbente.getId());
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());
        requestUpdate.setId(1L);

        ProcessResponse process = processController.updateProcess(requestUpdate);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidProcessExceptionUpdate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
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
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.searchProcesses(null,1L);

        Assert.assertEquals(1L,processes.size());
    }

    @Test
    void testGetProcessById(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        ProcessResponse processResponse = processController.getProcess(process.getId());

        Assertions.assertEquals(processResponse.getId(), process.getId());
        Assertions.assertEquals(processResponse.getQmax(), process.getQmax());
    }

    @Test
    void testSearchByAdsorbate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        List<Long> ids = new ArrayList<>();
        ids.add(adsorbate.getId());

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids));

        Assertions.assertEquals(1, searchResult.size());
        Assertions.assertTrue(searchResult.get(0).isRemovesAllAdsorbates());
        Assertions.assertEquals(1, searchResult.get(0).getProcesses().size());
        Assertions.assertEquals(0.65f, searchResult.get(0).getMaxQmax());

    }

    @Test
    void testSearchByAdsorbateRemoveOnlyOne(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestAdsorbate2 = new AdsorbateRequest("Prueba","PruebaIUPAC2",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);
        adsorbateController.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        List<Long> ids = new ArrayList<>();
        ids.add(adsorbate.getId());
        ids.add(2L);

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids));

        Assertions.assertEquals(1, searchResult.size());
        Assertions.assertFalse(searchResult.get(0).isRemovesAllAdsorbates());
        Assertions.assertEquals(1, searchResult.get(0).getProcesses().size());
        Assertions.assertEquals(0.65f, searchResult.get(0).getMaxQmax());
        Assertions.assertEquals(1, searchResult.get(0).getAdsorbent().getId());

    }

    @Test
    void testSearchByAdsorbateVariusProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestAdsorbate2 = new AdsorbateRequest("Prueba","PruebaIUPAC2",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);
        adsorbateController.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);
        processController.createProcess(request);

        List<Long> ids = new ArrayList<>();
        ids.add(adsorbate.getId());
        ids.add(2L);

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids));

        Assertions.assertEquals(1, searchResult.size());
        Assertions.assertFalse(searchResult.get(0).isRemovesAllAdsorbates());
        Assertions.assertEquals(2, searchResult.get(0).getProcesses().size());
        Assertions.assertEquals(0.65f, searchResult.get(0).getMaxQmax());
        Assertions.assertEquals("Prueba", searchResult.get(0).getAdsorbent().getName());

    }

    @Test
    void testGetProcessByIdNotFound(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            ProcessResponse processResponse = processController.getProcess(20L);
        });
    }
}
