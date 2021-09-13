package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.service.auth.AuthCodeService;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceTests {

    @Mock
    AuthCodeService authCodeService;

    @Autowired
    @InjectMocks
    AuthService authMockService = new AuthService();

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAuthCode() throws UserNotFoundException {
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.doNothing().when(authCodeService).generateAuthCode(any(User.class));
        authMockService.register(request);
        authMockService.resetPasswordGenerateCode(request);

    }
}
