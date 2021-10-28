package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.UserRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.UserResponse;
import fiuba.tpp.reactorapp.model.request.*;
import fiuba.tpp.reactorapp.model.response.*;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.repository.auth.TokenRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.service.ProcessService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@WithMockUser(username="admin",roles={"ADMIN"})
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

    @Autowired
    private AuthController authController;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void resetDatabase(){
        adsorbateRepository.deleteAll();
        adsorbentRepository.deleteAll();
        processRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

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

        List<ProcessResponse> processes = processController.getProcesses(getToken("prueba11"),"userAgent");
        Assert.assertEquals(1L,processes.size());
    }

    @ParameterizedTest
    @CsvSource({
            "'Bearer 12345'",
            "null",
            "''"
    })
    void testGetProcessesNoToken(String token){
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbente = adsorbentController.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbato = adsorbateController.createAdsorbate(requestAdsorbato);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbato.getId());
        request.setIdAdsorbent(adsorbente.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.getProcesses(token,"userAgent");
        Assert.assertEquals(0L,processes.size());
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
        ProcessResponse response = processController.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());

        ProcessResponse process = processController.updateProcess(response.getId(), requestUpdate);

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
        ProcessResponse response = processController.createProcess(request);
        processController.deleteProcess(response.getId());
        Assert.assertTrue(processController.getProcesses(getToken("prueba9"),"userAgent").isEmpty());
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

        List<ProcessResponse> processes = processController.searchProcesses(adsorbate.getId(),adsorbent.getId(), getToken("prueba8"),"userAgent");

        Assert.assertEquals(1L, processes.size());
    }

    @ParameterizedTest
    @CsvSource({
            "'Bearer 12345'",
            "null",
            "''"
    })
    void testSearchProcessNoToken(String  token){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processController.createProcess(request);

        List<ProcessResponse> processes = processController.searchProcesses(1L,1L, token,"userAgent");

        Assert.assertEquals(0L, processes.size());
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

        List<ProcessResponse> processes = processController.searchProcesses(adsorbate.getId(),null, getToken("prueba7"),"userAgent");

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

        List<ProcessResponse> processes = processController.searchProcesses(null,adsorbent.getId(), getToken("prueba6"),"userAgent");

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

        ProcessResponse processResponse = processController.getProcess(process.getId(), getToken("prueba5"),"userAgent");

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

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids),getToken("prueba4"),"userAgent");

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
        AdsorbateResponse adsorbate2 = adsorbateController.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        ProcessResponse process = processController.createProcess(request);

        List<Long> ids = new ArrayList<>();
        ids.add(adsorbate.getId());
        ids.add(adsorbate2.getId());

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids), getToken("prueba3"),"userAgent");

        Assertions.assertEquals(1, searchResult.size());
        Assertions.assertFalse(searchResult.get(0).isRemovesAllAdsorbates());
        Assertions.assertEquals(1, searchResult.get(0).getProcesses().size());
        Assertions.assertEquals(0.65f, searchResult.get(0).getMaxQmax());
        Assertions.assertEquals(adsorbent.getId(), searchResult.get(0).getAdsorbent().getId());

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

        List<SearchByAdsorbateResponse> searchResult = processController.searchBestAdsorbentByAdsorbates(new SearchByAdsorbateRequest(ids), getToken("prueba1"),"userAgent");

        Assertions.assertEquals(1, searchResult.size());
        Assertions.assertFalse(searchResult.get(0).isRemovesAllAdsorbates());
        Assertions.assertEquals(2, searchResult.get(0).getProcesses().size());
        Assertions.assertEquals(0.65f, searchResult.get(0).getMaxQmax());
        Assertions.assertEquals("Prueba", searchResult.get(0).getAdsorbent().getName());

    }

    @Test
    void testGetProcessByIdNotFound(){
        String token = getToken("prueba2");
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.getProcess(20L, token,"userAgent");
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
    void testCreateProcessInvalidKineticConstant() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        request.setReactionOrder(2);
        request.setKineticConstant(-10F);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.createProcess(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_KINETIC_CONSTANT.getMessage(),e.getReason());
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
    void testUpdateProcessInvalidKineticConstant(){
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
        requestUpdate.setReactionOrder(2);
        requestUpdate.setKineticConstant(-10F);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.updateProcess(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.INVALID_KINETIC_CONSTANT.getMessage(),e.getReason());
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
            "-1.0,2.0,10.0",
            "1.0,-2.0,10.0",
            "1.0,2.0,-10.0",
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
        ProcessResponse response = createProcess();
        Long processId = response.getId();
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            processController.calculateReactorVolume(processId,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_KINECT_INFORMATION.getMessage(),e.getReason());
    }

    private ProcessResponse createProcess(){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        return processController.createProcess(request);
    }

    private UserRequest createUserRequest(String placeholder){
        UserRequest user = new UserRequest();
        user.setName(placeholder);
        user.setSurname("Reimondo");
        user.setEmail(placeholder+"@gmail.com");
        user.setPassword("Prueba123");
        user.setRole(ERole.ROLE_ADMIN.name());
        user.setDescription("Es un usuario de prueba");
        return user;
    }

    private UserResponse createUserController(String placeholder){
        return authController.createUser(createUserRequest(placeholder));
    }

    private String getToken(String name){
        createUserController(name);
        LoginResponse response = authController.login(new AuthRequest(name + "@gmail.com", "Prueba123"),"userAgent");
        return  "Bearer " + response.getAccessToken();
    }

}
