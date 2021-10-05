package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.ReactorVolumeRequest;
import fiuba.tpp.reactorapp.model.response.ReactorVolumeResponse;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
class ProcessServiceTests {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AdsorbentService adsorbentService;

    @Autowired
    private AdsorbateService adsorbateService;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ProcessRepository processRepository;

    @AfterEach
    void resetDatabase(){
        adsorbentRepository.deleteAll();
        adsorbateRepository.deleteAll();
        processRepository.deleteAll();
    }


    @Test
    void testCreateProcess() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        Process process = processService.createProcess(request);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), request.getQmax());
    }

    @Test
    void testInvalidProcessExceptionCreate(){
        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(1L);
        request.setIdAdsorbent(1L);
        Assertions.assertThrows(InvalidProcessException.class, () -> {
            processService.createProcess(request);
        });
    }


    @Test
    void testFindAll() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        List<Process> processes = processService.getProcesses(false);
        Assert.assertEquals(1L,processes.size());
    }

    @Test
    void testFindAllOrderedByQMax() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        ProcessRequest request2 = new ProcessRequest(2.30f,1f,1f,1f,true,true,true);
        request2.setIdAdsorbate(adsorbate.getId());
        request2.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request2);

        List<Process> processes = processService.getProcesses(false);
        Process firstProcess = processes.get(0);
        Assert.assertEquals(2.30f, firstProcess.getQmax(), 0.1);
    }

    @Test
    void testUpdateProcess() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        Process processCreated = processService.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());

        Process process = processService.updateProcess(processCreated.getId(), requestUpdate);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidProcessExceptionUpdate() {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());

        Assertions.assertThrows(InvalidProcessException.class, () -> {
            processService.updateProcess(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteProcess() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        Process process = processService.createProcess(request);
        processService.deleteProcess(process.getId());
        Assert.assertTrue(processService.getProcesses(false).isEmpty());
    }

    @Test
    void testProcessNotFoundExceptionDelete() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            processService.deleteProcess(1L);
        });
    }

    @Test
    void testSearchProcess() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        AdsorbateRequest requestAdsorbate2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        Adsorbate adsorbate2 = adsorbateService.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        request.setIdAdsorbate(adsorbate2.getId());
        processService.createProcess(request);


        List<Process> reactores = processService.search(new ProcessFilter(null, adsorbent.getId()), false);
        Assert.assertEquals(2L, reactores.size());

    }

    @Test
    void testSearchProcessOrderByQmax() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        AdsorbateRequest requestAdsorbate2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        Adsorbate adsorbate2 = adsorbateService.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        ProcessRequest request2 = new ProcessRequest(2.65f,1f,1f,1f,true,true,true);
        request2.setIdAdsorbent(adsorbent.getId());
        request2.setIdAdsorbate(adsorbate2.getId());
        processService.createProcess(request2);


        List<Process> reactores = processService.search(new ProcessFilter(null, adsorbent.getId()), false);
        Assert.assertEquals(2L, reactores.size());
        Assert.assertEquals(2.65f, reactores.get(0).getQmax(), 0.0);

    }

    @Test
    void testSearchProcessAdsorbateAdsorbent() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        AdsorbateRequest requestAdsorbate2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        Adsorbate adsorbate2 = adsorbateService.createAdsorbate(requestAdsorbate2);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        request.setIdAdsorbate(adsorbate2.getId());
        processService.createProcess(request);

        List<Process> processes = processService.search(new ProcessFilter(adsorbate.getId(), adsorbent.getId()), false);
        Assert.assertEquals(1L, processes.size());

    }

    @Test
    void testGetProcessById() {
        Process process = createProcess();

        Long id = process.getId();
        Process foundProcess = processService.getById(id, false);
        Assertions.assertEquals(process.getQmax(), foundProcess.getQmax());
    }

    @Test
    void testGetProcessByIdNotFound() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
                processService.getById(20L, false);
        });
    }


    @Test
    void testDeleteAdsorbateWithProcess() {
        Process process = createProcess();
        adsorbateService.deleteAdsorbate(process.getAdsorbate().getId());
        List<Process> processes = processService.getProcesses(false);

        Assertions.assertTrue(processes.isEmpty());
    }

    @Test
    void testDeleteAdsorbentWithProcess() {
        Process process = createProcess();
        adsorbentService.deleteAdsorbent(process.getAdsorbent().getId());
        List<Process> processes = processService.getProcesses(false);
        Assertions.assertTrue(processes.isEmpty());
    }

    private Process createProcess() {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        return processService.createProcess(request);
    }

    private Process createProcessKinetic(Integer reactionOrder, Float kineticConstant) {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        request.setReactionOrder(reactionOrder);
        request.setKineticConstant(kineticConstant);
        return processService.createProcess(request);
    }

    @Test
    void testGetProcessCountAdsorbate() {
        Process process= createProcess();
        Assertions.assertEquals(1, adsorbateService.getAdsorbateProcessCount(process.getAdsorbate().getId()));
    }

    @Test
    void testGetProcessCountAdsorbateNoProcess(){
        Assertions.assertEquals(0, adsorbateService.getAdsorbateProcessCount(100L));
    }

    @Test
    void testGetProcessCountNoAdsorbate(){
        Assertions.assertEquals(0, adsorbateService.getAdsorbateProcessCount(null));
    }

    @Test
    void testGetProcessCountAdsorbent() {
        Process process = createProcess();
        Assertions.assertEquals(1, adsorbentService.getAdsorbentProcessCount(process.getAdsorbent().getId()));
    }

    @Test
    void testGetProcessCountAdsorbentNoProcess(){
        Assertions.assertEquals(0, adsorbentService.getAdsorbentProcessCount(1L));
    }

    @Test
    void testGetProcessCountNoAdsorbent(){
        Assertions.assertEquals(0, adsorbentService.getAdsorbentProcessCount(null));
    }

    @Test
    void testCalculateVolumeFirstOrder() {
        Process process = createProcessKinetic(1,10F);
        ReactorVolumeRequest request = new ReactorVolumeRequest(2.0,1.0,10.0);
        ReactorVolumeResponse response = processService.calculateVolume(process.getId(),request);

        Assertions.assertEquals(process.getId(), response.getProcess().getId());
        Assertions.assertEquals(0.69, response.getVolume(),0.01);
    }

    @Test
    void testCalculateVolumeSecondOrder() {
        Process process = createProcessKinetic(2,10F);
        ReactorVolumeRequest request = new ReactorVolumeRequest(2.0,1.0,10.0);
        ReactorVolumeResponse response = processService.calculateVolume(process.getId(),request);

        Assertions.assertEquals(process.getId(), response.getProcess().getId());
        Assertions.assertEquals(0.5, response.getVolume(),0.01);

    }

    @Test
    void testProcessNotFoundExceptionVolume() {
        ReactorVolumeRequest request = new ReactorVolumeRequest(2.0,1.0,10.0);
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            processService.calculateVolume(100L,request);
        });
    }

    @Test
    void testProcessNotKinecticInfoVolume() {
        ReactorVolumeRequest request = new ReactorVolumeRequest(2.0,1.0,10.0);
        Process process = createProcess();
        Long processId = process.getId();
        Assertions.assertThrows(InvalidProcessException.class, () -> {
            processService.calculateVolume(processId,request);
        });
    }

}
