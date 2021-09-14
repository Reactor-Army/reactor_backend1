package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.service.utils.CodeGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CodeGeneratorServiceTests {

    @Autowired
    CodeGeneratorService codeGeneratorService;

    @Test
    void generateRandomStringTest(){
        String code = codeGeneratorService.generateRandomStringAlphanumeric(6);
        Assertions.assertEquals(6, code.length());
    }
}
