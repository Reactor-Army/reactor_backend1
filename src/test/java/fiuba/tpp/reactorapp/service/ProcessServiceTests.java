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
    void testCreateProcess() throws InvalidProcessException {
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
    void testFindAll() throws InvalidProcessException {
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
    void testUpdateProcess() throws InvalidProcessException {
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
        requestUpdate.setId(1L);

        Process process = processService.updateProcess(requestUpdate);

        Assert.assertEquals(process.getAdsorbate().getId(), adsorbate.getId());
        Assert.assertEquals(process.getAdsorbent().getId(), adsorbent.getId());
        Assert.assertEquals(process.getQmax(), requestUpdate.getQmax());
    }

    @Test
    void testInvalidProcessExceptionUpdate() throws InvalidProcessException {
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
            requestUpdate.setId(2L);
            processService.updateProcess(requestUpdate);
        });
    }

    @Test
    void testDeleteProcess() throws InvalidProcessException, ComponentNotFoundException {
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
    void testSearchProcess() throws InvalidProcessException {
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
    void testSearchProcessAdsorbateAdsorbent() throws InvalidProcessException {
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
    void testGetProcessById() throws InvalidProcessException, ComponentNotFoundException {
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

    private Process createProcess() throws InvalidProcessException {
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
