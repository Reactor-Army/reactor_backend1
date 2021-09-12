package fiuba.tpp.reactorapp.service.auth;

import fiuba.tpp.reactorapp.entities.auth.AuthCode;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.repository.auth.AuthCodeRepository;
import fiuba.tpp.reactorapp.service.utils.CodeGeneratorService;
import fiuba.tpp.reactorapp.service.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthCodeService {

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodeGeneratorService codeGeneratorService;


    private static final int CODE_LENGTH = 6;
    private static final String EMAIL_SUBJECT = "Codigo renovacion de contraseña";

    public void generateAuthCode(User user){
        String randomString = codeGeneratorService.generateRandomStringAlphanumeric(CODE_LENGTH);
        Date time = Calendar.getInstance().getTime();
        AuthCode code = null;

        Optional<AuthCode> authCode = authCodeRepository.findByUser(user);
        if(authCode.isPresent()){
            code = authCode.get();
        }else{
            code = new AuthCode();
            code.setUser(user);
        }
        code.setCode(randomString);
        code.setRefreshDate(time);
        authCodeRepository.save(code);

        emailService.sendSimpleMessage(user.getEmail(), EMAIL_SUBJECT,randomString);
    }
}
