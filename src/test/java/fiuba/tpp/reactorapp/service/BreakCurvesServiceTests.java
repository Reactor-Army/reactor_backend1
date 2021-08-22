package fiuba.tpp.reactorapp.service;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
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
    void testEasyResult() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "1,3\n" +"2,5\n" +"3,7\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2.87, result.getThomasConstant(),0.01);
        Assertions.assertEquals(-3.48, result.getMaxConcentration(),0.01);
    }

    @Test
    void testEasyObservations() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "1,3\n" +"2,5\n" +"3,7\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        ThomasResponse result = breakCurvesService.calculateByThomas(request);

        Assertions.assertEquals(3, result.getObservations().size());
        Assertions.assertEquals(1, result.getObservations().get(0).getX(),0.01);
        Assertions.assertEquals(3.0, result.getObservations().get(0).getY(),0.01);
        Assertions.assertEquals(2, result.getObservations().get(1).getX(),0.01);
        Assertions.assertEquals(5.0, result.getObservations().get(1).getY(),0.01);
        Assertions.assertEquals(3, result.getObservations().get(2).getX(),0.01);
        Assertions.assertEquals(7.0, result.getObservations().get(2).getY(),0.01);

    }

    //TO DO REEMPLAZAR POR ANDREA TEST
    @Test
    void testCalculateResult() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" + "4,2\n" +"6,1\n").getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1000d,10d,1d);

        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(-4820.43, result.getThomasConstant(),0.01);
        Assertions.assertEquals(63.11, result.getMaxConcentration(),0.01);
    }

}
