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

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    private User registerUser(){
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba124");
        authService.register(request);
        Optional<User> user = userRepository.findByEmail("mati@gmail.com");
        return user.orElse(null);
    }

    @Test
    void generateCode(){
        User user = registerUser();
        Mockito.doNothing().when(emailService).sendSimpleMessage(anyString(),anyString(),anyString());
        authCodeMockService.generateAuthCode(user);

        Optional<AuthCode> code = authCodeRepository.findByUser(user);
        Assert.assertEquals(user.getEmail(),code.get().getUser().getEmail());
        Assert.assertEquals(6,code.get().getCode().length());
    }

    @Test
    void generateSecondCode() throws InterruptedException {
        User user = registerUser();
        Mockito.doNothing().when(emailService).sendSimpleMessage(anyString(),anyString(),anyString());
        authCodeMockService.generateAuthCode(user);
        Date now = Calendar.getInstance().getTime();
        TimeUnit.SECONDS.sleep(5);
        authCodeMockService.generateAuthCode(user);

        Optional<AuthCode> code = authCodeRepository.findByUser(user);
        Assert.assertTrue(now.before(code.get().getRefreshDate()));
    }

}
