package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.exception.DuplicateAdsorbentException;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import fiuba.tpp.reactorapp.model.exception.ComponentNotFoundException;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdsorbentServiceTests {

    @Autowired
    private AdsorbentService adsorbentService;


    @Test
    void testCreateAdsorbent() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        Adsorbent adsorbent = adsorbentService.createAdsorbent(request);

        Assert.assertEquals(adsorbent.getName(), request.getName());
        Assert.assertEquals(adsorbent.getParticleSize(), request.getParticleSize());
        Assert.assertEquals(adsorbent.getvBet(), request.getvBet());
    }

    @Test
    void testFindAll() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        List<Adsorbent> adsorbents = adsorbentService.getAll();
        Assert.assertEquals(1L, adsorbents.size());
    }

    @Test
    void testUpdateAdsorbent() throws ComponentNotFoundException, DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        Adsorbent updated = adsorbentService.updateAdsorbent(1L, requestUpdate);

        Assert.assertEquals(updated.getName(), requestUpdate.getName());
        Assert.assertEquals(updated.getParticleSize(), requestUpdate.getParticleSize());
        Assert.assertEquals(updated.getvBet(), requestUpdate.getvBet());
    }

    @Test
    void testUpdateAdsorbentExtraData() throws ComponentNotFoundException, DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        requestUpdate.setFormula("H2O");
        requestUpdate.setImpurities("Pedazos de algo");
        request.setSampleOrigin("Timbuctu");
        request.setSpeciesName("FloraCarbono");
        adsorbentService.createAdsorbent(request);
        Adsorbent updated = adsorbentService.updateAdsorbent(1L, requestUpdate);

        Assert.assertEquals(updated.getSampleOrigin(), requestUpdate.getSampleOrigin());
        Assert.assertEquals(updated.getFormula(), requestUpdate.getFormula());
        Assert.assertEquals(updated.getImpurities(), requestUpdate.getImpurities());
        Assert.assertEquals(updated.getSpeciesName(), requestUpdate.getSpeciesName());
    }

    @Test
    void testComponentNotFoundExceptionUpdate() {
        Assertions.assertThrows(ComponentNotFoundException.class, () -> {
            AdsorbentRequest requestUpdate = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
            adsorbentService.updateAdsorbent(2L, requestUpdate);
        });
    }

    @Test
    void testDeleteAdsorbent() throws ComponentNotFoundException, DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.deleteAdsorbent(1L);
        Assert.assertTrue(adsorbentService.getAll().isEmpty());

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
    void testSearchAdsorbentFilterName(long size, String filter) throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("Prueba2", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter(filter));
        Assert.assertEquals(size, adsorbents.size());
    }


    @Test
    void testSearchAdsorbentFilterUpperAndLowerName() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("PRUEBA", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("prueba", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter("prueba"));
        Assert.assertEquals(2L, adsorbents.size());
    }

    @Test
    void testSearchAdsorbentFilterAccent1() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("CARLOS", "Prueba2", 10f, 10f,10f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);
        List<Adsorbent> adsorbents = adsorbentService.search(new AdsorbentFilter("cárlos"));
        Assert.assertEquals(2L, adsorbents.size());
    }

    @Test
    void testCreateAdsorbentDuplicateNameAndParticleSize() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);

        Assertions.assertThrows(DuplicateAdsorbentException.class, () -> adsorbentService.createAdsorbent(request));
    }

    @Test
    void testUpdateDuplicateAdsorbent() throws DuplicateAdsorbentException {
        AdsorbentRequest request = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        AdsorbentRequest request2 = new AdsorbentRequest("CARLOS", "Prueba2", 10f, 10f,10f);
        AdsorbentRequest requestUpdate = new AdsorbentRequest("carlos", "Prueba", 1f, 1f,1f);
        adsorbentService.createAdsorbent(request);
        adsorbentService.createAdsorbent(request2);

        Assertions.assertThrows(DuplicateAdsorbentException.class, () -> adsorbentService.updateAdsorbent(2L,requestUpdate));
    }
}
