package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentNameResponse;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import fiuba.tpp.reactorapp.service.AdsorbentService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbentControllerTest {

    @Autowired
    private AdsorbentController adsorbentController;

    @Mock
    private AdsorbentService adsorbentService;

    @InjectMocks
    private AdsorbentController adsorbentMockController = new AdsorbentController();

    @Autowired
    private AuthController authController;

    @Autowired
    private AdsorbentRepository adsorbentRepository;


    @Test
    void testCreateAdsorbent(){
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentResponse adsorbent = adsorbentController.createAdsorbent(request);

        Assert.assertEquals(adsorbent.getName(), request.getName());
        Assert.assertEquals(adsorbent.getParticleSize(), request.getParticleSize());
        Assert.assertEquals(adsorbent.getvBet(), request.getvBet());
    }

    @Test
    void testCreateAdsorbentWithoutName(){
        AdsorbentRequest request = new AdsorbentRequest(null, "Prueba", 1f, 1f,1f);
        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.createAdsorbent(request));
    }

    @Test
    void testGetAllAdsorbents() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        List<AdsorbentResponse> adsorbents = adsorbentController.getAdsorbents(getToken());
        Assert.assertEquals(1L,adsorbents.size());
    }

    @ParameterizedTest
    @CsvSource({
            "'Bearer 12345'",
            "null",
            "''"
    })
    void testGetAllAdsorbentsNoToken(String token) {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        List<AdsorbentResponse> adsorbents = adsorbentController.getAdsorbents(token);
        Assert.assertEquals(0L,adsorbents.size());
    }


    @Test
    void testUpdateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        AdsorbentResponse r = adsorbentController.createAdsorbent(request);
        AdsorbentResponse updated = adsorbentController.updateAdsorbent(r.getId(), requestUpdate);

        Assert.assertEquals(updated.getName(), requestUpdate.getName());
        Assert.assertEquals(updated.getParticleSize(), requestUpdate.getParticleSize());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testComponentNotFoundExceptionUpdate(){
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(2L);
        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.updateAdsorbent(2L,requestUpdate));
    }

    @Test
    void testDeleteAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.deleteAdsorbent(1L);
        Assert.assertTrue(adsorbentController.getAdsorbents(getToken()).isEmpty());
    }

    @Test
    void testDeleteAdsorbentWithoutID() {
        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.deleteAdsorbent(1L));
    }


    @Test
    void testSearchAdsorbentsNoFilter() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbentes = adsorbentController.searchAdsorbents(null, getToken());
        Assert.assertEquals(2L,adsorbentes.size());
    }

    @ParameterizedTest
    @CsvSource({
            "'Bearer 12345'",
            "null",
            "''"
    })
    void testSearchAdsorbentsNoFilterNoToken(String token) {
        addFreeAdsorbent();
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbentes = adsorbentController.searchAdsorbents(null, token);
        Assert.assertEquals(1L,adsorbentes.size());
    }


    @Test
    void testSearchAdsorbentsFilterName() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbents = adsorbentController.searchAdsorbents("Prueba2", getToken());
        Assert.assertEquals(1L,adsorbents.size());
    }

    @Test
    void testSearchAdsorbentFilterUpperAndLowerName() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentResponse> adsorbatos = adsorbentController.searchAdsorbents("PRUEBA", getToken());
        Assert.assertEquals(2L,adsorbatos.size());
    }

    @Test
    void testSearchAdsorbentName() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentNameResponse> adsorbentsName = adsorbentController.searchAdsorbentsName("PRUEBA", null, getToken());
        Assert.assertEquals(2L,adsorbentsName.size());
        Assert.assertEquals("PRUEBA (Prueba)", adsorbentsName.get(0).getName());
        Assert.assertEquals("Prueba (Prueba2)", adsorbentsName.get(1).getName());
    }

    @Test
    void testSearchAdsorbentNameOneMatch() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("EsteNoEs", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);
        List<AdsorbentNameResponse> adsorbentsName = adsorbentController.searchAdsorbentsName("PRUEBA", null, getToken());
        Assert.assertEquals(1L,adsorbentsName.size());
        Assert.assertEquals("PRUEBA (Prueba)", adsorbentsName.get(0).getName());

    }

    @Test
    void testSearchAdsorbentNameSizeNull() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", null, 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        List<AdsorbentNameResponse> adsorbentsName = adsorbentController.searchAdsorbentsName("PRUEBA", null, getToken());
        Assert.assertEquals(1L,adsorbentsName.size());
        Assert.assertEquals("PRUEBA (-)", adsorbentsName.get(0).getName());
    }

    @Test
    void testFindById(){
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "60", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        AdsorbentResponse adsorbent = adsorbentController.getAdsorbent(1L, getToken());
        Assert.assertEquals("PRUEBA", adsorbent.getName());
        Assert.assertEquals("60", adsorbent.getParticleSize());
    }

    @Test
    void testGetAdsorbentByIdNotFound(){
        String token = getToken();
        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.getAdsorbent(20L, token));
    }

    @Test
    void testCreateDuplicateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "60", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);
        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.createAdsorbent(request));
    }

    @Test
    void testUpdateDuplicateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentController.createAdsorbent(request);
        adsorbentController.createAdsorbent(request2);

        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setId(1L);

        Assertions.assertThrows(ResponseStatusException.class, () -> adsorbentController.createAdsorbent(request));
    }

    @Test
    void testUpdateAdsorbentInternalError() {
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Mockito.when(adsorbentService.updateAdsorbent(1L,requestUpdate)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentMockController.updateAdsorbent(1L, requestUpdate);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testCreateAdsorbentInternalError() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Mockito.when(adsorbentService.createAdsorbent(request)).thenThrow(RuntimeException.class);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentMockController.createAdsorbent(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());

    }

    @Test
    void testDeleteAdsorbentInternalError() {
        Mockito.doThrow(RuntimeException.class).when(adsorbentService).deleteAdsorbent(1L);

        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            adsorbentMockController.deleteAdsorbent(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    private void addFreeAdsorbent(){
        Adsorbent adsorbent = new Adsorbent("PRUEBA", "Prueba", 1f, 1f,1f);
        adsorbent.setFree(true);
        adsorbentRepository.save(adsorbent);
    }

    private String getToken(){
        authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new AuthRequest("mati@gmail.com", "Prueba123"));
        return  "Bearer " + response.getAccessToken();
    }
}
