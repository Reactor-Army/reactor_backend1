package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.auth.AuthCode;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.repository.auth.AuthCodeRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.service.auth.AuthCodeService;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import fiuba.tpp.reactorapp.service.utils.EmailService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthCodeServiceTests {

    @Mock
    private EmailService emailService;

    @Autowired
    @InjectMocks
    private final AuthCodeService authCodeMockService = new AuthCodeService();

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void generateCode(){
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba124");
        authService.register(request);
        Optional<User> user = userRepository.findByEmail("mati@gmail.com");
        Mockito.doNothing().when(emailService).sendSimpleMessage(anyString(),anyString(),anyString());
        authCodeMockService.generateAuthCode(user.get());

        Optional<AuthCode> code = authCodeRepository.findByUser(user.get());
        Assert.assertEquals(user.get().getEmail(),code.get().getUser().getEmail());
        Assert.assertEquals(6,code.get().getCode().length());
    }
}
