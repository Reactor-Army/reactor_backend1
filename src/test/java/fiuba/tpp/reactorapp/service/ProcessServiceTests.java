package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.exception.DuplicateIUPACNameException;
import fiuba.tpp.reactorapp.model.exception.InvalidProcessException;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProcessServiceTests {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AdsorbentService adsorbentService;

    @Autowired
    private AdsorbateService adsorbateService;


    @Test
    void testCreateProcess() throws InvalidProcessException, DuplicateIUPACNameException {
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
        Assertions.assertThrows(InvalidProcessException.class, () -> {
            ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
            request.setIdAdsorbate(1L);
            request.setIdAdsorbent(1L);
            processService.createProcess(request);
        });
    }


    @Test
    void testFindAll() throws InvalidProcessException, DuplicateIUPACNameException {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        List<Process> processes = processService.getAll();
        Assert.assertEquals(1L,processes.size());
    }

    @Test
    void testFindAllOrderedByQMax() throws InvalidProcessException, DuplicateIUPACNameException {
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

        List<Process> processes = processService.getAll();
        Process firstProcess = processes.get(0);
        Assert.assertEquals(2.30f, firstProcess.getQmax(), 0.1);
    }

    @Test
    void testUpdateProcess() throws InvalidProcessException, DuplicateIUPACNameException {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        ProcessRequest requestUpdate = new ProcessRequest(65f,1f,1f,1f,true,true,true);
        requestUpdate.setIdAdsorbate(adsorbate.getId());
        requestUpdate.setIdAdsorbent(adsorbent.getId());

        Process process = processService.updateProcess(1L, requestUpdate);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidProcessExceptionUpdate() throws InvalidProcessException, DuplicateIUPACNameException {
        AdsorbentRequest requestAdsorbente = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbente);

        AdsorbateRequest requestAdsorbato = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbato);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);

        Assertions.assertThrows(InvalidProcessException.class, () -> {

            ProcessRequest requestUpdate = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
            requestUpdate.setIdAdsorbate(adsorbate.getId());
            requestUpdate.setIdAdsorbent(adsorbent.getId());
            processService.updateProcess(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteProcess() throws InvalidProcessException, ComponentNotFoundException, DuplicateIUPACNameException {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        processService.createProcess(request);
        processService.deleteProcess(1L);
        Assert.assertTrue(processService.getAll().isEmpty());
    }

    @Test
    void testProcessNotFoundExceptionDelete() throws InvalidProcessException, ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            processService.deleteProcess(1L);
        });
    }

    @Test
    void testSearchProcess() throws InvalidProcessException, DuplicateIUPACNameException {
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


        List<Process> reactores = processService.search(new ProcessFilter(null, 1L));
        Assert.assertEquals(2L, reactores.size());

    }

    @Test
    void testSearchProcessOrderByQmax() throws InvalidProcessException, DuplicateIUPACNameException {
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


        List<Process> reactores = processService.search(new ProcessFilter(null, 1L));
        Assert.assertEquals(2L, reactores.size());
        Assert.assertEquals(2.65f, reactores.get(0).getQmax(), 0.0);

    }

    @Test
    void testSearchProcessAdsorbateAdsorbent() throws InvalidProcessException, DuplicateIUPACNameException {
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

        List<Process> processes = processService.search(new ProcessFilter(1L, 1L));
        Assert.assertEquals(1L, processes.size());

    }

    @Test
    void testGetProcessById() throws InvalidProcessException, ComponentNotFoundException, DuplicateIUPACNameException {
        Process process = createProcess();

        Long id = process.getId();
        Process foundProcess = processService.getById(id);
        Assertions.assertEquals(process.getQmax(), foundProcess.getQmax());
    }

    @Test
    void testGetProcessByIdNotFound() throws InvalidProcessException, ComponentNotFoundException {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
                processService.getById(20L);
        });
    }

    @Test
    void testDeleteAdsorbateWithProcess() throws InvalidProcessException, ComponentNotFoundException, DuplicateIUPACNameException {
        createProcess();
        adsorbateService.deleteAdsorbate(1L);
        List<Process> processes = processService.getAll();

        Assertions.assertTrue(processes.isEmpty());
    }

    @Test
    void testDeleteAdsorbentWithProcess() throws InvalidProcessException, ComponentNotFoundException, DuplicateIUPACNameException {
        createProcess();
        adsorbentService.deleteAdsorbent(1L);
        List<Process> processes = processService.getAll();
        Assertions.assertTrue(processes.isEmpty());
    }

    private Process createProcess() throws InvalidProcessException, DuplicateIUPACNameException {
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        Adsorbate adsorbate = adsorbateService.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        return processService.createProcess(request);
    }
}
