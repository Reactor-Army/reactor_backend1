package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BreakCurvesServiceTests {

    @Autowired
    private BreakCurvesService breakCurvesService;

    @Test
    void testEasyResult(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("x,y\n" + "1,3\n" +"2,5\n" +"3,7\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file);

        RegressionResult result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2, result.getSlope(),0.01);
        Assertions.assertEquals(1, result.getIntercept(),0.01);
    }

}
