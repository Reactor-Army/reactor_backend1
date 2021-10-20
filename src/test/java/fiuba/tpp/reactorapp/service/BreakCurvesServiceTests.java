package fiuba.tpp.reactorapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fiuba.tpp.reactorapp.entities.BreakCurvesData;
import fiuba.tpp.reactorapp.entities.EModel;
import fiuba.tpp.reactorapp.model.dto.FileTemplateDTO;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.BreakCurvesDataResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import fiuba.tpp.reactorapp.repository.BreakCurvesDataRepository;
import fiuba.tpp.reactorapp.scheduler.BreakCurvesDataScheduler;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BreakCurvesServiceTests {

    @Autowired
    private BreakCurvesService breakCurvesService;

    @Autowired
    private BreakCurvesDataRepository breakCurvesDataRepository;

    @Autowired
    private BreakCurvesDataScheduler breakCurvesDataScheduler;


    @Test
    void testEasyObservations() throws JsonProcessingException {
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
    void testThomasXLSX() throws IOException {
        MultipartFile file = new MockMultipartFile("filename", "thomas.xlsx", "application/vnd.ms-excel", new ClassPathResource("testFiles/thomas.xlsx").getInputStream());
        ThomasRequest request = new ThomasRequest(file,0.5,42.1,4.612);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2.37, result.getThomasConstant(),0.01);
        Assertions.assertEquals(0.62, result.getMaxConcentration(),0.01);

    }

    @Test
    void testThomasXLS() throws IOException {
        MultipartFile file = new MockMultipartFile("filename", "thomas.xls", "application/vnd.ms-excel", new ClassPathResource("testFiles/thomas.xls").getInputStream());
        ThomasRequest request = new ThomasRequest(file,0.5,42.1,4.612);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2.37, result.getThomasConstant(),0.01);
        Assertions.assertEquals(0.62, result.getMaxConcentration(),0.01);

    }

    @Test
    void testThomasWithDataFromTesis() throws JsonProcessingException {
        MockMultipartFile file = dataFromTesisThomas();
        ThomasRequest request = new ThomasRequest(file,0.5,42.1,4.612);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(2.37, result.getThomasConstant(),0.01);
        Assertions.assertEquals(0.62, result.getMaxConcentration(),0.01);
        Assertions.assertEquals(0.99, result.getRms(),0.01);
        Assertions.assertNotNull(result.getDataId());
    }


    @Test
    void testYoonNelsonWithDataFromTesis() throws JsonProcessingException {
        MockMultipartFile file = dataFromTesisThomas();
        YoonNelsonRequest request = new YoonNelsonRequest(file,0.0005);
        YoonNelsonResponse result = breakCurvesService.calculateByYoonNelson(request);
        Assertions.assertEquals(0.1, result.getYoonNelsonConstant(),0.01);
        Assertions.assertEquals(136.314, result.getTimeFiftyPercent(),0.01);
        Assertions.assertEquals(0.99, result.getRms(),0.01);
        Assertions.assertNotNull(result.getDataId());

    }

    @Test
    void testAdamsBohartWithDataFromTesis() throws JsonProcessingException {
        MockMultipartFile file = dataFromTesisAdams();
        AdamsBohartRequest request = new AdamsBohartRequest(file,0.5,42.1,0.4723,15.0);
        AdamsBohartResponse result = breakCurvesService.calculateByAdamsBohart(request);
        Assertions.assertEquals(1.61, result.getAdamsBohartConstant(),0.01);
        Assertions.assertEquals(0.195, result.getMaxAbsorptionCapacity(),0.01);
        Assertions.assertEquals(0.97, result.getRms(),0.01);
        Assertions.assertNotNull(result.getDataId());
    }

    @Test
    void testThomasWithJuancho() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        ThomasRequest request = new ThomasRequest(file,0.9494,8D,20D);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Assertions.assertEquals(0.01974, result.getThomasConstant(),0.0001);
        Assertions.assertEquals(16.57392, result.getMaxConcentration(),0.0001);
        Assertions.assertEquals(0.96, result.getRms(),0.01);
    }


    @Test
    void testYoonNelsonWithJuancho() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        YoonNelsonRequest request = new YoonNelsonRequest(file,0.941);
        YoonNelsonResponse result = breakCurvesService.calculateByYoonNelson(request);
        Assertions.assertEquals(0.1564, result.getYoonNelsonConstant(),0.0001);
        Assertions.assertEquals(44.033, result.getTimeFiftyPercent(),0.001);
        Assertions.assertEquals(0.96, result.getRms(),0.01);

    }

    @Test
    void testAdamsBohartWithJuancho() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        AdamsBohartRequest request = new AdamsBohartRequest(file,0.95041,8D,0.24,5D);
        AdamsBohartResponse result = breakCurvesService.calculateByAdamsBohart(request);
        Assertions.assertEquals(0.00299, result.getAdamsBohartConstant(),0.00001);
        Assertions.assertEquals(31.299, result.getMaxAbsorptionCapacity(),0.001);
        Assertions.assertEquals(0.72, result.getRms(),0.02);
    }

    @Test
    void testGetFileDTO() throws IOException {
        FileTemplateDTO dto = breakCurvesService.getDataTemplateFile();
        Assertions.assertEquals("datos.xlsx", dto.getFileName());
        Assertions.assertNotNull(dto.getResource());
    }

    @Test
    void testAdamsBohartGetData() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        AdamsBohartRequest request = new AdamsBohartRequest(file,0.95041,8D,0.24,5D);
        AdamsBohartResponse result = breakCurvesService.calculateByAdamsBohart(request);
        BreakCurvesDataResponse data = breakCurvesService.getBreakCurveData(result.getDataId());
        Assert.assertTrue(data.getRequest() instanceof AdamsBohartRequest);
        Assert.assertTrue(data.getResponse() instanceof AdamsBohartResponse);
    }

    @Test
    void testThomasGetData() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        ThomasRequest request = new ThomasRequest(file,0.9494,8D,20D);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        BreakCurvesDataResponse data = breakCurvesService.getBreakCurveData(result.getDataId());
        Assert.assertTrue(data.getRequest() instanceof ThomasRequest);
        Assert.assertTrue(data.getResponse() instanceof ThomasResponse);
    }

    @Test
    void testYoonNelsonGetData() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        YoonNelsonRequest request = new YoonNelsonRequest(file,0.941);
        YoonNelsonResponse result = breakCurvesService.calculateByYoonNelson(request);
        BreakCurvesDataResponse data = breakCurvesService.getBreakCurveData(result.getDataId());
        Assert.assertTrue(data.getRequest() instanceof YoonNelsonRequest);
        Assert.assertTrue(data.getResponse() instanceof YoonNelsonResponse);
    }

    @Test
    void testDeleteData() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        ThomasRequest request = new ThomasRequest(file,0.9494,8D,20D);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        breakCurvesService.deleteBreakCurveData(result.getDataId());
        Long id = result.getDataId();
        Assert.assertThrows(ComponentNotFoundException.class, () ->{
            breakCurvesService.getBreakCurveData(id);
        });
    }

    @Test
    @Transactional
    void testDeleteDataCustomQuery() throws JsonProcessingException {
        MockMultipartFile file = dataFromJuancho();
        ThomasRequest request = new ThomasRequest(file,0.9494,8D,20D);
        ThomasResponse result = breakCurvesService.calculateByThomas(request);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        breakCurvesDataRepository.deleteAllByNameNullAndUploadDateBefore(calendar.getTime());
        Long id = result.getDataId();
        Assert.assertThrows(ComponentNotFoundException.class, () ->{
            breakCurvesService.getBreakCurveData(id);
        });

    }

    @Test
    void testDeleteDataScheduler() throws JsonProcessingException {
        Calendar calendar = Calendar.getInstance();

        BreakCurvesData d1 = new BreakCurvesData();
        d1.setUploadDate(calendar.getTime());
        d1.setModel(EModel.THOMAS);
        breakCurvesDataRepository.save(d1);

        calendar.add(Calendar.DAY_OF_MONTH, -2);
        BreakCurvesData d2 = new BreakCurvesData();
        d2.setUploadDate(calendar.getTime());
        breakCurvesDataRepository.save(d2);

        breakCurvesDataScheduler.cleanBreakCurvesDataJob();

        Long id = d2.getId();
        Assert.assertThrows(ComponentNotFoundException.class, () ->{
            breakCurvesService.getBreakCurveData(id);
        });

        Long idNoBorrrado = d1.getId();
        Assert.assertTrue(breakCurvesDataRepository.findById(idNoBorrrado).isPresent());

    }



    private MockMultipartFile dataFromTesisThomas(){
        return new MockMultipartFile("thomas","thomas.csv",MediaType.TEXT_PLAIN_VALUE,tesisData().getBytes());
    }

    private MockMultipartFile dataFromTesisAdams(){
        return new MockMultipartFile("adams","adams.csv",MediaType.TEXT_PLAIN_VALUE,tesisDataAdams().getBytes());
    }

    private MockMultipartFile dataFromJuancho(){
        return new MockMultipartFile("juancho","juancho.csv",MediaType.TEXT_PLAIN_VALUE,datosJuancho().getBytes());
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

    private String tesisDataAdams(){
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
                "0.07200,0.73158\n";
    }

    private String datosJuancho(){
        return "volumenEfluente,C/C0\n"+
                "2.823,0\n"+
                "5.646,0\n"+
                "8.469,0\n"+
                "11.292,0\n"+
                "14.115,0\n"+
                "16.938,0\n"+
                "19.761,0\n"+
                "22.584,0\n"+
                "25.407,0\n"+
                "28.23,0\n"+
                "31.053,0\n"+
                "33.876,0.285\n"+
                "39.522,0.498\n"+
                "42.345,0.536\n"+
                "45.168,0.752\n"+
                "47.991,0.798\n"+
                "50.814,0.822\n"+
                "53.637,0.833\n"+
                "56.46,0.805\n"+
                "59.283,0.836\n"+
                "62.106,0.898\n"+
                "64.929,0.903\n"+
                "67.752,0.878\n"+
                "70.575,0.876\n"+
                "73.398,0.903\n"+
                "76.221,0.86\n"+
                "79.044,0.93\n"+
                "81.867,0.931\n"+
                "84.69,0.938\n";
    }

}
