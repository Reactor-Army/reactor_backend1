package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
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
    void testEasyObservations() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                ("volumenEfluente,C/C0\n" +
                        "0.07050,0.64700\n" +
                        "0.07200,0.73158\n" +
                        "0.07350,0.76736\n" ).getBytes()
        );
        ThomasRequest request = new ThomasRequest(file,1d,10d,1d);

        ThomasResponse result = breakCurvesService.calculateByThomas(request);

        Assertions.assertEquals(3, result.getObservations().size());
        Assertions.assertEquals(0.07050, result.getObservations().get(0).getX(),0.01);
        Assertions.assertEquals(0.64700, result.getObservations().get(0).getY(),0.01);
        Assertions.assertEquals(0.07200, result.getObservations().get(1).getX(),0.01);
        Assertions.assertEquals(0.73158, result.getObservations().get(1).getY(),0.01);
        Assertions.assertEquals(0.07350, result.getObservations().get(2).getX(),0.01);
        Assertions.assertEquals(0.76736, result.getObservations().get(2).getY(),0.01);

    }

    @Test
    void testThomasWithDataFromTesis(){
        MockMultipartFile file = dataFromTesisThomas();
        ThomasRequest request = new ThomasRequest(file,0.5,42.1,4.612);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2.37, result.getThomasConstant(),0.01);
        Assertions.assertEquals(0.62, result.getMaxConcentration(),0.01);
    }

    //NO AJUSTA
    @Test
    void testYoonNelsonWithDataFromTesis(){
        MockMultipartFile file = dataFromTesisThomas();
        YoonNelsonRequest request = new YoonNelsonRequest(file,0.0005);
        YoonNelsonResponse result = breakCurvesService.calculateByYoonNelson(request);
        Assertions.assertEquals(0.009, result.getYoonNelsonConstant(),0.01);
        Assertions.assertEquals(310.675, result.getTimeFiftyPercent(),0.01);

    }

    private MockMultipartFile dataFromTesisThomas(){
        return new MockMultipartFile("thomas","thomas.csv",MediaType.TEXT_PLAIN_VALUE,tesisData().getBytes());
    }

    private String tesisData(){
        return "volumenEfluente,C/C0\n" +
                "0.00000,0.00000\n" +
                "0.00150,-0.0019\n" +
                "0.00300,0.00781\n" +
                "0.00450,-0.0035\n" +
                "0.00600,0.02895\n" +
                "0.00750,0.00455\n" +
                "0.00900,-0.0084\n" +
                "0.01050,0.00130\n" +
                "0.01200,-0.0032\n" +
                "0.01350,0.00618\n" +
                "0.01500,0.01106\n" +
                "0.01650,0.00130\n" +
                "0.01800,-0.0019\n" +
                "0.01950,-0.0035\n" +
                "0.02100,0.01757\n" +
                "0.02250,0.00130\n" +
                "0.02400,-32.500\n" +
                "0.02550,-0.0084\n" +
                "0.02700,0.00130\n" +
                "0.02850,-32.500\n" +
                "0.03000,0.00455\n" +
                "0.03150,0.01269\n" +
                "0.03300,0.02895\n" +
                "0.03450,0.00130\n" +
                "0.03600,0.00130\n" +
                "0.03750,-0.0084\n" +
                "0.03900,-0.0019\n" +
                "0.04050,-0.0084\n" +
                "0.04200,-0.0084\n" +
                "0.04350,0.01106\n" +
                "0.04500,0.01919\n" +
                "0.04650,-0.0084\n" +
                "0.04800,0.00943\n" +
                "0.04950,-0.0035\n" +
                "0.05100,0.00130\n" +
                "0.05250,0.00130\n" +
                "0.05400,0.01594\n" +
                "0.05550,0.06473\n" +
                "0.05700,0.04359\n" +
                "0.05850,0.10377\n" +
                "0.06000,0.12491\n" +
                "0.06150,0.18184\n" +
                "0.06300,0.25340\n" +
                "0.06450,0.33147\n" +
                "0.06600,0.42418\n" +
                "0.06750,0.47948\n" +
                "0.06900,0.57381\n" +
                "0.07050,0.64700\n" +
                "0.07200,0.73158\n" +
                "0.07350,0.76736\n" +
                "0.07500,0.82754\n" +
                "0.07650,0.83730\n" +
                "0.07800,0.84380\n" +
                "0.07950,0.86983\n" +
                "0.08100,0.88284\n" +
                "0.08250,0.87959\n" +
                "0.08400,0.90398\n" +
                "0.08550,0.90236\n" +
                "0.08700,0.90561\n" +
                "0.08850,0.92675\n" +
                "0.09000,0.91374\n" +
                "0.09150,0.93326\n" +
                "0.09300,0.94952\n" +
                "0.09450,0.93489\n" +
                "0.09600,0.94464\n" +
                "0.09750,0.97229\n" +
                "0.09900,0.96254\n" +
                "0.10050,0.96741\n" +
                "0.10200,0.97717\n" +
                "0.10350,1.00157\n" +
                "0.10500,0.99344\n" +
                "0.10650,0.98368\n" +
                "0.10800,0.99019\n" +
                "0.10950,0.98205\n" +
                "0.11100,0.99994\n" +
                "0.11250,1.03085\n" +
                "0.11400,0.99181\n" +
                "0.11550,0.99019\n" +
                "0.11700,1.00157\n" +
                "0.11850,0.99506\n" +
                "0.12000,1.01621\n" +
                "0.12150,1.02109\n" +
                "0.12300,1.02759\n" +
                "0.12450,1.03085\n" +
                "0.12600,1.03410\n" +
                "0.12750,1.03410\n" +
                "0.12900,1.03247\n" +
                "0.13050,1.03410\n" +
                "0.13200,1.03410\n" +
                "0.13350,1.0341\n";
    }

}
