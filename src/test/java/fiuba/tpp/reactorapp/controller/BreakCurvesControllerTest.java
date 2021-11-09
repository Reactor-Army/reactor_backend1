package fiuba.tpp.reactorapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.request.BreakCurveDataRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BreakCurvesControllerTest {

    @Autowired
    private BreakCurvesController breakCurvesController;

    @Autowired
    private ProcessController processController;

    @Mock
    private BreakCurvesService breakCurvesService;

    @InjectMocks
    private BreakCurvesController breakCurvesMockController = new BreakCurvesController();

    @InjectMocks
    private ProcessController processMockController = new ProcessController();

    @Test
    void testInvalidFile(){
        ThomasRequest request = new ThomasRequest();
        Errors errors = new BeanPropertyBindingResult(request, "request");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
    }

    @Test
    void testEmptyFile(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",new byte[0]);

        ThomasRequest request = new ThomasRequest(file,1d,1d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
    }

    @Test
    void testNullFile(){
        ThomasRequest request = new ThomasRequest(null,1d,1d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
        Assert.assertEquals(ResponseMessage.FILE_NOT_FOUND.getMessage(),e.getReason());
    }

    @Test
    void testFileNotCSV(){
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,concentracionSalida\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
        Assert.assertEquals(ResponseMessage.INVALID_FILE.getMessage(),e.getReason());
    }

    @Test
    void testInvalidHeaderCSV(){
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("x,concentracionSalida\n" + "1,0.005\n" +"2,0.01\n" +"3,0.1\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        assertDoesNotThrow(() -> breakCurvesController.thomas(request, errors));
    }

    @Test
    void testInvalidHeaderCSV2(){
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("concentracionSalida,x\n" + "0.005,1\n" +"0.01,2\n" +"0.1,3\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        assertDoesNotThrow(() -> breakCurvesController.thomas(request, errors));
    }

    @Test
    void testNoHeaderCSV(){
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("1,0.005\n" +"2,0.01\n" +"3,0.1\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        assertDoesNotThrow(() -> breakCurvesController.thomas(request, errors));
    }


    @Test
    void testInvalidHeaderCSV3(){
        MockMultipartFile file
                = new MockMultipartFile(
                "hello",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("C/C0,x\n" + "0.005,1\n" +"0.01,2\n" +"0.1,3\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        assertDoesNotThrow(() -> breakCurvesController.thomas(request, errors));
    }

    @ParameterizedTest
    @CsvSource({
            "csv",
            "xls",
            "xlsx",
    })
    void testMockResponse(String fileExtension) throws JsonProcessingException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello." + fileExtension,
                MediaType.TEXT_PLAIN_VALUE,
                "blabla".getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,1d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Mockito.when(breakCurvesService.calculateByThomas(request)).thenReturn(new ThomasResponse(1,1));

        ThomasResponse result = breakCurvesMockController.thomas(request, errors);

        Assertions.assertEquals(1, result.getThomasConstant(),0.01);
        Assertions.assertEquals(1, result.getMaxConcentration(),0.01);
    }



    @ParameterizedTest
    @CsvSource({
            ",1.0 ,1.0",
            "1.0, ,1.0",
            "1.0,1.0,",
            ",,,",
            "1.0,0.0,1.0",
            "0.0,1.0,1.0",
            "1.0,1.0,0.0",
    })
    void testInvalidRequestResponse(Double caudal, Double ci, Double sorbenteReactor){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,caudal,ci,sorbenteReactor);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
    }

    @ParameterizedTest
    @CsvSource({
            ",1.0 ,1.0, 1.0",
            "1.0, ,1.0,1.0",
            "1.0,1.0,,1.0",
            "1.0,1.0,1.0,",
            ",,,,",
            "1.0,0.0,1.0,1.0",
            "0.0,1.0,1.0,1.0",
            "1.0,1.0,0.0, 1.0",
            "1.0,1.0,1.0, 0.0",
    })
    void testInvalidRequestResponseAdams(Double caudal, Double ci, Double uo,Double z){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "1,2\n" +"2,4\n").getBytes()
        );

        AdamsBohartRequest request = new AdamsBohartRequest(file,caudal,ci,uo,z);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.adamsBohart(request, errors);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0.0",
            "null",
    }, nullValues = {"null"})
    void testInvalidRequestResponseYoonNelson(Double caudal){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "1,2\n" +"2,4\n").getBytes()
        );

        YoonNelsonRequest request = new YoonNelsonRequest(file,caudal);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.yoonNelson(request, errors);
        });
    }

    @Test
    void testJsonErrorThomas() throws JsonProcessingException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv" ,
                MediaType.TEXT_PLAIN_VALUE,
                "blabla".getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,1d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Mockito.when(breakCurvesService.calculateByThomas(request)).thenThrow(JsonProcessingException.class);

        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.thomas(request, errors);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorAdams() throws JsonProcessingException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv" ,
                MediaType.TEXT_PLAIN_VALUE,
                "blabla".getBytes()
        );

        AdamsBohartRequest request = new AdamsBohartRequest(file,1.0,1.0,2.0,1.0);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Mockito.when(breakCurvesService.calculateByAdamsBohart(request)).thenThrow(JsonProcessingException.class);

        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.adamsBohart(request, errors);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorYoonNelson() throws JsonProcessingException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv" ,
                MediaType.TEXT_PLAIN_VALUE,
                "blabla".getBytes()
        );
        YoonNelsonRequest request = new YoonNelsonRequest(file,1.0);

        Errors errors = new BeanPropertyBindingResult(request, "request");
        Mockito.when(breakCurvesService.calculateByYoonNelson(request)).thenThrow(JsonProcessingException.class);

        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.yoonNelson(request, errors);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorSaveData() throws JsonProcessingException {
        BreakCurveDataRequest request = new BreakCurveDataRequest(1L,"PruebaError", false);
        Mockito.when(breakCurvesService.saveBreakCurveData(1L,request)).thenThrow(JsonProcessingException.class);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.saveBreakCurveData(1L,request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorGetByProcess() throws JsonProcessingException {
        Mockito.when(breakCurvesService.getBreakCurvesDataByProcess(1L)).thenThrow(JsonProcessingException.class);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            processMockController.getBreakCurvesDataByProcess(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,''",
            "null, 'nombre'",
            "0, 'nombre'",
            "1, null"
    }, nullValues = {"null"})
    void testInvalidSaveData(Long id, String nombre) throws JsonProcessingException {
        BreakCurveDataRequest request = new BreakCurveDataRequest(id,nombre, false);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.saveBreakCurveData(1L,request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_BREAK_CURVE_DATA.getMessage(), e.getReason());
    }

    @Test
    void testBreakCurvesDataNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.getBreakCurveData(1000L);
        });
        Assert.assertEquals(ResponseMessage.DATA_NOT_FOUND.getMessage(), e.getReason());
    }

    @Test
    void testBreakCurvesDataByProcessNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            processController.getBreakCurvesDataByProcess(1000L);
        });
        Assert.assertEquals(ResponseMessage.PROCESS_NOT_FOUND.getMessage(), e.getReason());
    }

    @Test
    void testDeleteBreakCurvesDataNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.deleteBreakCurveData(1000L);
        });
        Assert.assertEquals(ResponseMessage.DATA_NOT_FOUND.getMessage(), e.getReason());
    }

    @Test
    void testDownloadFile(){
        ResponseEntity<ByteArrayResource> fileResponse = breakCurvesController.downloadDataTemplate();
        Assert.assertEquals(HttpStatus.OK, fileResponse.getStatusCode());
    }

    @Test
    void testBreakCurvesDataFreeThomasNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.getFreeThomas();
        });
        Assert.assertEquals(ResponseMessage.DATA_NOT_FOUND.getMessage(), e.getReason());
    }

    @Test
    void testBreakCurvesDataFreeYoonNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.getFreeYoonNelson();
        });
        Assert.assertEquals(ResponseMessage.DATA_NOT_FOUND.getMessage(), e.getReason());
    }
    @Test
    void testBreakCurvesDataFreeAdamsNotFound()  {
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.getFreeAdams();
        });
        Assert.assertEquals(ResponseMessage.DATA_NOT_FOUND.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorGetFreeThomas() throws JsonProcessingException {
        Mockito.when(breakCurvesService.getFreeData(EModel.THOMAS)).thenThrow(JsonProcessingException.class);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.getFreeThomas();
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorGetFreeYoon() throws JsonProcessingException {
        Mockito.when(breakCurvesService.getFreeData(EModel.YOON_NELSON)).thenThrow(JsonProcessingException.class);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.getFreeYoonNelson();
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testJsonErrorGetFreeAdams() throws JsonProcessingException {
        Mockito.when(breakCurvesService.getFreeData(EModel.ADAMS_BOHART)).thenThrow(JsonProcessingException.class);
        ResponseStatusException e =Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesMockController.getFreeAdams();
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }
}
