package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.NotEnoughObservationsException;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MathServiceTests {

    @Autowired
    private MathService mathService;

    private Process createProcessKinetic(Integer reactionOrder, Float kineticConstant){
        Process process = new Process();
        process.setKineticConstant(kineticConstant);
        process.setReactionOrder(reactionOrder);
        return process;
    }

    @Test
    void testCalculateVolumeFirstOrder() {
        Process process = createProcessKinetic(1,10F);
        double volume = mathService.resolveFirstOrder(process,2.0,1.0,10.0);
        Assertions.assertEquals(0.69, volume,0.01);
    }

    @Test
    void testCalculateVolumeSecondOrder() {
        Process process = createProcessKinetic(2,10F);
        double volume = mathService.resolveSecondOrder(process,2.0,1.0,10.0);
        Assertions.assertEquals(0.5, volume,0.01);

    }

    // Testeamos  y = 2x
    @Test
    void testSimpleRegression(){
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(1,2));
        observations.add(new Observation(2,4));

        RegressionResult result = mathService.calculateRegression(observations);

        Assertions.assertEquals(2, result.getSlope(),0.01);
    }

    // Testeamos  y = 2x + 1
    @Test
    void testSimpleRegressionWithInterception(){
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(1,3));
        observations.add(new Observation(2,5));
        observations.add(new Observation(0,1));

        RegressionResult result = mathService.calculateRegression(observations);

        Assertions.assertEquals(2, result.getSlope(),0.01);
        Assertions.assertEquals(1, result.getIntercept(),0.01);
    }

    // Probamos una regresion con varios datos que sabemos el resultado
    // y = -0,84 x + 11,4
    @Test
    void testRealRegression(){
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(7,2));
        observations.add(new Observation(1,9));
        observations.add(new Observation(10,2));
        observations.add(new Observation(5,5));
        observations.add(new Observation(4,7));
        observations.add(new Observation(3,11));
        observations.add(new Observation(13,2));
        observations.add(new Observation(10,5));
        observations.add(new Observation(2,14));


        RegressionResult result = mathService.calculateRegression(observations);

        Assertions.assertEquals(-0.84, result.getSlope(),0.01);
        Assertions.assertEquals(11.48, result.getIntercept(),0.01);
    }

    @Test
    void testNotEnoughtObservations(){
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(7,2));

        Assertions.assertThrows(NotEnoughObservationsException.class , () -> {
            mathService.calculateRegression(observations);
        });
    }





}
