package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.request.LoginRequest;
import fiuba.tpp.reactorapp.model.auth.request.RegisterRequest;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;
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
        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request);
        });
    }

    @Test
    void testEmptyFile(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes()
        );
        ThomasRequest request = new ThomasRequest(file);

        Assert.assertThrows(ResponseStatusException.class, () ->{
            breakCurvesController.thomas(request);
        });
    }

    @Test
    void testMockResponse(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "blabla".getBytes()
        );
        ThomasRequest request = new ThomasRequest(file);

        Mockito.when(breakCurvesService.calculateByThomas(request)).thenReturn(new RegressionResult(1,1));

        RegressionResult result = breakCurvesMockController.thomas(request);

        Assertions.assertEquals(1, result.getSlope(),0.01);
        Assertions.assertEquals(1, result.getIntercept(),0.01);


    }

    @Test
    void testEasyResponse(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("x,y\n" + "1,2\n" +"2,4\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file);

        RegressionResult result = breakCurvesController.thomas(request);
        Assertions.assertEquals(2, result.getSlope(),0.01);
    }



}
