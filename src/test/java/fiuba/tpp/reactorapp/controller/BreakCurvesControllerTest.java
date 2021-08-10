package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.service.BreakCurvesService;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BreakCurvesControllerTest {

    @Autowired
    private BreakCurvesController breakCurvesController;

    @Mock
    private BreakCurvesService breakCurvesService;




}
