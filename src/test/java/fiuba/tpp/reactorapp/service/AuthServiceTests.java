package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.repository.auth.AuthCodeRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AuthServiceTests {

    @Mock
    AuthCodeService authCodeService;

    @Autowired
    @InjectMocks
    AuthService authMockService = new AuthService();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        authCodeRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void testAuthCode(){
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.doNothing().when(authCodeService).generateAuthCode(any(User.class));
        authMockService.register(request);
        assertDoesNotThrow(()->authMockService.resetPasswordGenerateCode(request));
    }
}
