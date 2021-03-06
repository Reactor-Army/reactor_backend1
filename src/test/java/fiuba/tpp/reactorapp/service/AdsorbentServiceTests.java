package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.DuplicateAdsorbentException;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.repository.AdsorbentRepository;
import org.junit.Assert;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;

@SpringBootTest
class AdsorbentServiceTests {

    @Autowired
    private AdsorbentService adsorbentService;

    @Autowired
    private AdsorbentRepository adsorbentRepository;

    @AfterEach
    void resetDatabase(){
        adsorbentRepository.deleteAll();
    }


    @Test
    void testCreateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);

        Assert.assertEquals(adsorbent.getName(), request.getName());
        Assert.assertEquals(adsorbent.getParticleSize(), request.getParticleSize());
        Assert.assertEquals(adsorbent.getvBet(), request.getvBet());
    }

    @Test
    void testFindAll() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        List<Adsorbent> adsorbents = adsorbentService.getAdsorbents(false);
        Assert.assertEquals(1L, adsorbents.size());
    }

    @Test
    void testUpdateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);
        Adsorbent updated = adsorbentService.updateAdsorbent(adsorbent.getId(), requestUpdate);

        Assert.assertEquals(updated.getName(), requestUpdate.getName());
        Assert.assertEquals(updated.getParticleSize(), requestUpdate.getParticleSize());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testUpdateAdsorbentExtraData() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setFormula("H2O");
        requestUpdate.setImpurities("Pedazos de algo");
        request.setSampleOrigin("Timbuctu");
        request.setSpeciesName("FloraCarbono");
        Adsorbent response = adsorbentService.createAdsorbent(request);
        Adsorbent updated = adsorbentService.updateAdsorbent(response.getId(), requestUpdate);

        Assert.assertEquals(updated.getSampleOrigin(), requestUpdate.getSampleOrigin());
        Assert.assertEquals(updated.getFormula(), requestUpdate.getFormula());
        Assert.assertEquals(updated.getImpurities(), requestUpdate.getImpurities());
        Assert.assertEquals(updated.getSpeciesName(), requestUpdate.getSpeciesName());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() {
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            adsorbentService.updateAdsorbent(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);
        adsorbentService.deleteAdsorbent(adsorbent.getId());
        Assert.assertTrue(adsorbentService.getAdsorbents(false).isEmpty());

    }

    @Test
    void testComponentNotFoundExceptionDelete() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> adsorbentService.deleteAdsorbent(2L));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'ba2'",
            "2, ",
            "2, ''"
    })
    void testSearchAdsorbentFilterName(long size, String filter) {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter(filter), false);
        Assert.assertEquals(size, adsorbents.size());
    }


    @Test
    void testSearchAdsorbentFilterUpperAndLowerName() {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter("prueba"), false);
        Assert.assertEquals(2L, adsorbents.size());
    }

    @Test
    void testSearchAdsorbentFilterAccent1() {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("CARLOS", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter("c??rlos"), false);
        Assert.assertEquals(2L, adsorbents.size());
    }

    @Test
    void testCreateAdsorbentDuplicateNameAndParticleSize() {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);

        Assertions.assertThrows(DuplicateAdsorbentException.class, () -> adsorbentService.createAdsorbent(request));
    }

    @Test
    void testUpdateDuplicateAdsorbent() {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("CARLOS", "Prueba2", 10f, 10f,10f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request2);

        Long adsorbentId = adsorbent.getId();
        Assertions.assertThrows(DuplicateAdsorbentException.class, () -> adsorbentService.updateAdsorbent(adsorbentId,requestUpdate));
    }

    @Test
    void testSearchIdAdsorbarte() {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("CARLOS", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter("c??rlos",1L), false);
        Assert.assertEquals(0L, adsorbents.size());
    }
}
