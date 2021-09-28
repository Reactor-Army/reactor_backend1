package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.request.AdsorbateRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbateNameResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbateResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.service.AdsorbateService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin",roles={"ADMIN"})
class AdsorbateControllerTest {

    @Autowired
    private AdsorbateController adsorbateController;

    @Mock
    private AdsorbateService adsorbateService;

    @InjectMocks
    private AdsorbateController adsorbateMockController = new AdsorbateController();

    @Test
    void testCreateAdsorbate(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);

        AdsorbateResponse adsorbate = adsorbateController.createAdsorbate(request);

        Assert.assertEquals(adsorbate.getIonName(), request.getIonName());
        Assert.assertEquals(adsorbate.getIonCharge(), request.getIonCharge());
        Assert.assertEquals(adsorbate.getIonRadius(), request.getIonRadius());
        Assert.assertEquals(1L, (long) adsorbate.getId());
    }

    @Test
    void testCreateAdsorbateWithoutName(){
        AdsorbateRequest request = new AdsorbateRequest(null,"PruebaIUPAC",1,1f,10f);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.createAdsorbate(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_ADSORBATE.getMessage(),e.getReason());
    }

    @Test
    void testUpdateAdsorbate(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        adsorbateController.createAdsorbate(request);
        AdsorbateResponse updated = adsorbateController.updateAdsorbate(1L, requestUpdate);

        Assert.assertEquals(updated.getIonName(), requestUpdate.getIonName());
        Assert.assertEquals(updated.getIonCharge(), requestUpdate.getIonCharge());
        Assert.assertEquals(updated.getIonRadius(), requestUpdate.getIonRadius());
    }

    @Test
    void testUpdateAdsorbateMolarMass(){
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",12,10f,100f);
        requestUpdate.setMolarMass(2f);
        adsorbateController.createAdsorbate(request);
        AdsorbateResponse updated = adsorbateController.updateAdsorbate(1L, requestUpdate);

        Assert.assertEquals(updated.getMolarMass(), requestUpdate.getMolarMass());
    }

    @Test
    void testUpdateAdsorbateThatNotExist() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","PruebaIUPAC",1,10f,100f);
        adsorbateController.createAdsorbate(request);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.updateAdsorbate(2L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.ADSORBATE_NOT_FOUND.getMessage(),e.getReason());
    }

    @Test
    void testDeleteAdsorbate() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.deleteAdsorbate(1L);
        Assert.assertTrue(adsorbateController.getAdsorbates().isEmpty());
    }

    @Test
    void testDeleteAdsorbateWithoutID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.deleteAdsorbate(1L);
        });
    }

    @Test
    void testGetAllAdsorbates() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateController.createAdsorbate(request);
        List<AdsorbateResponse> adsorbates = adsorbateController.getAdsorbates();
        Assert.assertEquals(1L,adsorbates.size());
    }

    @Test
    void testGetAllAdsorbatesCharge() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",-1,1f,10f);
        adsorbateController.createAdsorbate(request);
        List<AdsorbateResponse> adsorbates = adsorbateController.getAdsorbates();
        Assert.assertEquals("1-",adsorbates.get(0).getIonChargeFormula());
    }

    @Test
    void testSearchAdsorbatesNoFilter() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates(null,null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbatesFilterIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbates = adsorbateController.searchAdsorbates("IUPAC2",null);
        Assert.assertEquals(1L,adsorbates.size());
    }

    @Test
    void testSearchAdsorbateFilterIUPACAndCharge() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("IUPAC2",1);
        Assert.assertEquals(1L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterCharge() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","PruebaIUPAC",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("Prueba2","PruebaIUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates(null,1);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLowerIUPAC() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("PRUEBA",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateFilterUpperAndLower() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateResponse> adsorbatos = adsorbateController.searchAdsorbates("CARLOS",null);
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbateName() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","PRUEBA",1,1f,10f);
        AdsorbateRequest request2 = new AdsorbateRequest("carlos","prueba2",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        adsorbateController.createAdsorbate(request2);
        List<AdsorbateNameResponse> adsorbatesNames = adsorbateController.searchAdsorbatesName("CARLOS", null);
        Assert.assertEquals(2L,adsorbatesNames.size());
        Assert.assertEquals("CARLOS (PRUEBA)", adsorbatesNames.get(0).getName());
        Assert.assertEquals("Carlos (prueba2)", adsorbatesNames.get(1).getName());
    }

    @Test
    void testCreateAdsorbateNameIUPACNull() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS",null,1,1f,10f);
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.createAdsorbate(request);
        });
    }

    @Test
    void testFindById(){
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","carlos IUPAC",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        AdsorbateResponse adsorbate = adsorbateController.getAdsorbate(1L);
        Assert.assertEquals("CARLOS", adsorbate.getIonName());
        Assert.assertEquals("carlos IUPAC", adsorbate.getNameIUPAC());
    }

    @Test
    void testGetAdsorbateByIdNotFound(){
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
           adsorbateController.getAdsorbate(20L);
        });
        Assert.assertEquals(ResponseMessage.ADSORBATE_NOT_FOUND.getMessage(),e.getReason());
    }

    @Test
    void testCreateAdsorbateNameIUPACDuplicate() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","IUPAC",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.createAdsorbate(request);
        });
        Assert.assertEquals(ResponseMessage.DUPLICATE_ADSORBATE.getMessage(),e.getReason());
    }

    @Test
    void testUpdateAdsorbateNameIUPACDuplicate() {
        AdsorbateRequest request = new AdsorbateRequest("CARLOS","IUPAC",1,1f,10f);
        adsorbateController.createAdsorbate(request);
        AdsorbateRequest request2 = new AdsorbateRequest("CARLOS","IUPAC2",1,1f,10f);
        adsorbateController.createAdsorbate(request2);

        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","IUPAC2",1,10f,100f);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateController.updateAdsorbate(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.DUPLICATE_ADSORBATE.getMessage(),e.getReason());

    }

    @Test
    void testUpdateAdsorbateInternalError() {
        AdsorbateRequest requestUpdate = new AdsorbateRequest("Prueba2","IUPAC2",1,10f,100f);
        Mockito.when(adsorbateService.updateAdsorbate(1L,requestUpdate)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateMockController.updateAdsorbate(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testCreateAdsorbateInternalError() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba2","IUPAC2",1,10f,100f);
        Mockito.when(adsorbateService.createAdsorbate(request)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateMockController.createAdsorbate(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testDeleteAdsorbateInternalError() {
        Mockito.doThrow(RuntimeException.class).when(adsorbateService).deleteAdsorbate(1L);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbateMockController.deleteAdsorbate(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    @Test
    void testAddAdsorbateWithoutRegulationIsFalse() {
        AdsorbateRequest request = new AdsorbateRequest("Prueba","IUPAC2",1,10f,100f);
        AdsorbateResponse response = adsorbateController.createAdsorbate(request);
        Assertions.assertFalse(response.getRegulated());
    }

}
