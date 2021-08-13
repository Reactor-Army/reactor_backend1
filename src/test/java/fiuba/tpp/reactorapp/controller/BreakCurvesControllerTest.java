package fiuba.tpp.reactorapp.controller;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import fiuba.tpp.reactorapp.model.auth.request.LoginRequest;
import fiuba.tpp.reactorapp.model.auth.request.RegisterRequest;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BreakCurvesControllerTest {

    @Autowired
    private BreakCurvesController breakCurvesController;

    @Mock
    private BreakCurvesService breakCurvesService;

    @InjectMocks
    private BreakCurvesController breakCurvesMockController = new BreakCurvesController();

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
                ("x,concentracionSalida\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
        Assert.assertEquals(ResponseMessage.INVALID_HEADER.getMessage(),e.getReason());
    }

    @Test
    void testMockResponse() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
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

    @Test
    void testEasyResponse(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,concentracionSalida\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        ThomasResponse result = breakCurvesController.thomas(request, errors);
        Assertions.assertEquals(0.1, result.getThomasConstant(),0.01);
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
                ("volumenEfluente,concentracionSalida\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,caudal,ci,sorbenteReactor);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request, errors);
        });
    }

}
