package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authMockController = new AuthController();



    @Test
    void testRegisterUserAndLogin(){
        authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new AuthRequest("mati@gmail.com", "Prueba123"));
        Assert.assertEquals("mati@gmail.com", response.getEmail());
        Assert.assertEquals("ROLE_USER", response.getRoles().get(0));
    }

    @Test
    void testRegisterUser(){
        RegisterResponse response = authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
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
        AuthRequest request = new AuthRequest("" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request);
        });
    }

    @Test
    void testInvalidLogin(){
        authController.registerUser(new AuthRequest("matias@gmail.com" ,"Prueba123"));
        AuthRequest request = new AuthRequest("matiTest2", "Prueba123");
        Assert.assertThrows(BadCredentialsException.class, () ->{
            authController.authenticateUser(request);
        });
    }

    @Test
    void testInvalidRegisterDuplicateEmail(){
        AuthRequest request = new AuthRequest("mati@gmail.com" ,"Prueba123");
        authController.registerUser(request);
        AuthRequest request2 = new AuthRequest("mati@gmail.com" ,"Prueba123");
        Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.registerUser(request2);
        });
    }

    @Test
    void testRegisterUserInternalErrror() {
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.when(authService.register(request)).thenThrow(RuntimeException.class);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authMockController.registerUser(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    @Test
    void testAuthCodeEmailNotFound(){
        AuthRequest request = new AuthRequest("lucas@gmail.com","");
        assertDoesNotThrow(() -> authController.generateCodeResetPassword(request));
    }

    @ParameterizedTest
    @CsvSource({
            "''",
            "null",
            "'matigmail.com'"
    })
    void testAuthCodeInvalidEmail(String email){
        AuthRequest request = new AuthRequest(email,"");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.generateCodeResetPassword(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_REGISTER.getMessage(),e.getReason());
    }

    @Test
    void testAuthCode() throws UserNotFoundException {
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.doNothing().when(authService).resetPasswordGenerateCode(request);
        authController.registerUser(request);
        assertDoesNotThrow(()->authMockController.generateCodeResetPassword(request));
    }

    @Test
    void testResetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest("123456","Prueba123");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.resetPassword(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

}
