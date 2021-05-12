package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.request.LoginRequest;
import fiuba.tpp.reactorapp.model.auth.request.RegisterRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Test
    void testRegisterUserAndLogin(){
        authController.registerUser(new RegisterRequest("mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new LoginRequest("mati@gmail.com", "Prueba123"));
        Assert.assertEquals("mati@gmail.com", response.getEmail());
        Assert.assertEquals("ROLE_USER", response.getRoles().get(0));
    }

    @Test
    void testRegisterUser(){
        RegisterResponse response = authController.registerUser(new RegisterRequest("mati@gmail.com","Prueba123"));
        Assert.assertEquals("mati@gmail.com", response.getEmail());
        Assert.assertEquals("ROLE_USER", response.getRoles().get(0));
    }

    @ParameterizedTest
    @CsvSource({
            "''",
            "null",
            "'matigmail.com'"
    })
    void testInvalidEmail(String email){
        RegisterRequest request = new RegisterRequest("" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidLogin(){
        authController.registerUser(new RegisterRequest("matias@gmail.com" ,"Prueba123"));
        LoginRequest request = new LoginRequest("matiTest2", "Prueba123");
        Assert.assertThrows(BadCredentialsException.class, () ->{
            authController.authenticateUser(request);
        });
    }

    @Test
    void testInvalidRegisterDuplicateEmail(){
        RegisterRequest request = new RegisterRequest("mati@gmail.com" ,"Prueba123");
        authController.registerUser(request);
        RegisterRequest request2 = new RegisterRequest("mati@gmail.com" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request2);
        });
    }
}
