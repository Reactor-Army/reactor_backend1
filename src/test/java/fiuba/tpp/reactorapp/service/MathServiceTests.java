package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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



}
