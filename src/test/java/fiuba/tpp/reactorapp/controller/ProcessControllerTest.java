package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.*;
import fiuba.tpp.reactorapp.model.response.*;
import fiuba.tpp.reactorapp.service.ProcessService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    ProcessService processService;

    @InjectMocks
    ProcessController processMockController = new ProcessController();


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

        ProcessResponse process = processController.updateProcess(1L, requestUpdate);

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

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.updateProcess(2L, requestUpdate);
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
            processController.getProcess(20L);
        });
    }

    @Test
    void testProcessCountAdsorbate(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        ProcessCountResponse count = adsorbateController.getAdsorbateProcessCount(adsorbate.getId());

        Assertions.assertEquals(1, count.getProcessCount());
    }

    @Test
    void testProcessCountAdsorbent(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        ProcessCountResponse count = adsorbentController.getAdsorbentProcessCount(adsorbent.getId());

        Assertions.assertEquals(1, count.getProcessCount());
    }

    @Test
    void testUpdateProcessInternalError() {
        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        Mockito.when(processService.updateProcess(1L,requestUpdate)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processMockController.updateProcess(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testCreateProcessInternalError() {
        ProcessRequest request = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(1L);
        request.setIdAdsorbent(1L);
        Mockito.when(processService.createProcess(request)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processMockController.createProcess(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testDeleteAdsorbentInternalError() {
        Mockito.doThrow(RuntimeException.class).when(processService).deleteProcess(1L);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processMockController.deleteProcess(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    @Test
    void testCreateProcessInvalidReactionOrder() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        request.setReactionOrder(3);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.createProcess(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_REACTION_ORDER.getMessage(),e.getReason());
    }

    @Test
    void testUpdateProcessInvalidReactionOrder(){
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
        requestUpdate.setReactionOrder(3);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.updateProcess(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.INVALID_REACTION_ORDER.getMessage(),e.getReason());
    }

    @Test
    void testProcessNotFoundExceptionVolume(){
        ReactorVolumeRequest request = new ReactorVolumeRequest(1.5, 1.0,20.0);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.calculateReactorVolume(1L,request);
        });
        Assert.assertEquals(ResponseMessage.PROCESS_NOT_FOUND.getMessage(),e.getReason());
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, ,",
            ", 2.0,",
            ", ,20.0",
            "1.0, 2.0,",
    })
    void testInvalidVolumeRequest(Double ci, Double cf, Double flow){
        ReactorVolumeRequest request = new ReactorVolumeRequest(ci, cf,flow);
        createProcess();
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.calculateReactorVolume(1L,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_VOLUME_REQUEST.getMessage(),e.getReason());
    }

    @Test
    void testProcessWithNoKinecticInfo(){
        ReactorVolumeRequest request = new ReactorVolumeRequest(1.5, 1.0,20.0);
        createProcess();
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.calculateReactorVolume(1L,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_KINECT_INFORMATION.getMessage(),e.getReason());
    }

    private void createProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);
    }

}
