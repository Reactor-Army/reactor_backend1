package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.request.ProcessRequest;
import fiuba.tpp.reactorapp.model.request.TesisFileRequest;
import fiuba.tpp.reactorapp.model.response.*;
import fiuba.tpp.reactorapp.repository.AdsorbateRepository;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.repository.ProcessRepository;
import fiuba.tpp.reactorapp.repository.TesisFileRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@WithMockUser(username="admin",roles={"ADMIN"})
class TesisFileControllerTest {

    @Autowired
    private TesisFileController tesisFileController;

    @Autowired
    private ProcessController processController;

    @Autowired
    private AdsorbentController adsorbentController;

    @Autowired
    private AdsorbateController adsorbateController;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @Autowired
    private AdsorbateRepository adsorbateRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private TesisFileRepository tesisFileRepository;

    @AfterEach
    void resetDatabase(){
        tesisFileRepository.deleteAll();
        adsorbateRepository.deleteAll();
        adsorbentRepository.deleteAll();
        processRepository.deleteAll();
    }


    private ProcessResponse createProcces(String nameAdsorbent, String nameAdsorbate){
        AdsorbentRequest requestAdsorbent = new AdsorbentRequest(nameAdsorbent, "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(requestAdsorbent);

        AdsorbateRequest requestAdsorbate = new AdsorbateRequest(nameAdsorbate,nameAdsorbate,1,1f,10f);
        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(requestAdsorbate);

        ProcessRequest request = new ProcessRequest(0.65f,1f,1f,1f,true,true,true);
        request.setIdAdsorbate(adsorbate.getId());
        request.setIdAdsorbent(adsorbent.getId());
        return processController.createProcess(request);
    }



    @Test
    void testFileTooBig(){
        byte[] bytes = new byte[55000000];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.uploadTesisFile(request);
        });
        Assert.assertEquals(ResponseMessage.TESIS_FILE_SIZE_EXCEED.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testFileEmpty(){
        byte[] bytes = new byte[0];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.uploadTesisFile(request);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testFileNull(){
        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",null, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.uploadTesisFile(request);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testInvalidType(){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.uploadTesisFile(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_TESIS_FILE.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource({
            "pdf",
            "doc",
            "docx",
    })
    void testUploadFileAllExtensions(String extension){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello."+extension, MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");

        assertDoesNotThrow(() -> tesisFileController.uploadTesisFile(request));
    }

    @Test
    void testUploadFileNoProcess(){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");

        TesisFileResponse response = tesisFileController.uploadTesisFile(request);

        Assert.assertEquals("hello", response.getFilename());
        Assert.assertEquals("pdf", response.getType());
        Assert.assertTrue(response.getProcesses().isEmpty());
    }

    @Test
    void testUploadFileOneProcess(){
        ProcessResponse responseProcess = createProcces("adsorbent1", "adsorbate1");
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),String.valueOf(responseProcess.getId()));

        TesisFileResponse response = tesisFileController.uploadTesisFile(request);

        Assert.assertEquals("hello", response.getFilename());
        Assert.assertEquals("Autor de prueba", response.getAuthor());
        Assert.assertEquals("pdf", response.getType());
        Assert.assertFalse(response.getProcesses().isEmpty());
    }

    @Test
    void testUploadFileTwoProcess(){
        ProcessResponse p1 = createProcces("ads1","ads2");
        ProcessResponse p2 = createProcces("ads3","ads4");
        String ids = String.valueOf(p1.getId()).concat(",").concat(String.valueOf(p2.getId()));
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),ids);

        TesisFileResponse response = tesisFileController.uploadTesisFile(request);

        Assert.assertEquals("hello", response.getFilename());
        Assert.assertEquals("Prueba", response.getName());
        Assert.assertEquals("pdf", response.getType());
        Assert.assertEquals(2, response.getProcesses().size());
    }

    @Test
    void testDownloadTesisFile(){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");

        TesisFileResponse response = tesisFileController.uploadTesisFile(request);
        ResponseEntity<ByteArrayResource> fileResponse = tesisFileController.downloadTesis(response.getId());
        Assert.assertEquals(HttpStatus.OK, fileResponse.getStatusCode());
    }

    @Test
    void testDownloadTesisFileNotExist(){
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.downloadTesis(1000L);
        });
        Assert.assertEquals(ResponseMessage.TESIS_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());

    }

    private TesisFileResponse uploadFile(){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");

        return tesisFileController.uploadTesisFile(request);
    }

    private TesisFileResponse uploadFile(String name, String author){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest(name,author,file, Calendar.getInstance().getTime(),"");

        return tesisFileController.uploadTesisFile(request);
    }

    @Test
    void testUpdateFileTooBig(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[55000000];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(id,request);
        });
        Assert.assertEquals(ResponseMessage.TESIS_FILE_SIZE_EXCEED.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testFileChangeEmpty(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[0];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(id,request);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testFileChangeNull(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",null, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(id,request);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testChangeInvalidType(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(id,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_TESIS_FILE.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource({
            "pdf",
            "doc",
            "docx",
    })
    void testChangeFileAllExtensions(String extension){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello."+extension, MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");

        assertDoesNotThrow(() -> tesisFileController.changeTesisFile(id,request));
    }

    @Test
    void testChangeNotExistent(){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(1000L,request);
        });
        Assert.assertEquals(ResponseMessage.TESIS_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testChangeFile(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "goodbye",
                "goodbye.docx", MediaType.ALL_VALUE,
                bytes);

        TesisFileRequest request = new TesisFileRequest("Prueba","Autor de prueba",file, Calendar.getInstance().getTime(),"");
        TesisFileResponse change = tesisFileController.changeTesisFile(id,request);

        Assert.assertEquals("goodbye", change.getFilename());
        Assert.assertEquals("docx", change.getType());

    }

    @Test
    void testDeleteFile(){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        assertDoesNotThrow(() -> tesisFileController.deleteTesisFile(id));
    }

    @Test
    void testDeleteNotExistent(){
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.deleteTesisFile(1000L);
        });
        Assert.assertEquals(ResponseMessage.TESIS_NOT_FOUND.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource(value = {
            ",prueba",
            "prueba,",
            "prueba,null",
            "null,prueba"
    }, nullValues = {"null"})
    void testUploadFileNoNameNoAuthor(String name, String author){
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);
        TesisFileRequest request = new TesisFileRequest(name,author,file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.uploadTesisFile(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_TESIS_REQUEST.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource(value = {
            ",prueba",
            "prueba,",
            "prueba,null",
            "null,prueba"
    }, nullValues = {"null"})
    void testChangeFileNoNameNoAuthor(String name, String author){
        TesisFileResponse response = uploadFile();
        Long id = response.getId();
        byte[] bytes = new byte[10];
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.pdf", MediaType.ALL_VALUE,
                bytes);
        TesisFileRequest request = new TesisFileRequest(name,author,file, Calendar.getInstance().getTime(),"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            tesisFileController.changeTesisFile(id,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_TESIS_REQUEST.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());

    }

    @Test
    void testSearchFiles(){
        TesisFileResponse f1 = uploadFile("busqueda1","MATIAS");
        TesisFileResponse f2 = uploadFile("busqueda2","LUCARIO");

        List<TesisFileResponse> l1 = tesisFileController.searchFiles("mati");
        List<TesisFileResponse> l2 = tesisFileController.searchFiles("BUSQUE");

        Assert.assertEquals(2,l2.size());
        Assert.assertEquals(1, l1.size());
        Assert.assertEquals("MATIAS", l1.get(0).getAuthor());
    }

    @Test
    void testSearchFilesNoResult(){
        uploadFile("busqueda1","MATIAS");
        uploadFile("busqueda2","LUCARIO");

        List<TesisFileResponse> l1 = tesisFileController.searchFiles("tesisNoExiste");

        Assertions.assertTrue(l1.isEmpty());



    }

}
