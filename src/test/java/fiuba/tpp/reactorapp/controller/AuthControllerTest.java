package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.exception.InvalidRegisterException;
import fiuba.tpp.reactorapp.model.auth.request.LoginRequest;
import fiuba.tpp.reactorapp.model.auth.request.RegisterRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Test
    void testRegisterUserAndLogin(){
        authController.registerUser(new RegisterRequest("matiTest","mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new LoginRequest("matiTest", "Prueba123"));
        Assert.assertEquals("matiTest", response.getUsername());
        Assert.assertEquals("ROLE_USER", response.getRoles().get(0));
    }

    @Test
    void testInvalidRegisterInvalidName(){
        RegisterRequest request = new RegisterRequest("","mati@gmail.com","Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidRegisterNullName(){
        RegisterRequest request = new RegisterRequest(null,"mati@gmail.com","Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidRegisterEmptyEmail(){
        RegisterRequest request = new RegisterRequest("matiTest","" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidRegisterInvalidEmail(){
        RegisterRequest request = new RegisterRequest("matiTest","matigmail.com" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidRegisterNullEmail(){
        RegisterRequest request = new RegisterRequest("matiTest",null ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidLogin(){
        authController.registerUser(new RegisterRequest("matiTest","matias@gmail.com" ,"Prueba123"));
        LoginRequest request = new LoginRequest("matiTest2", "Prueba123");
        Assert.assertThrows(BadCredentialsException.class, () ->{
            authController.authenticateUser(request);
        });
    }


}
