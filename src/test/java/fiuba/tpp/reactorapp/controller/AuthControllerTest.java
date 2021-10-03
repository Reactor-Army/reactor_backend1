package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.auth.AuthCode;
import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.request.UserRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.auth.response.RoleResponse;
import fiuba.tpp.reactorapp.model.auth.response.UserResponse;
import fiuba.tpp.reactorapp.repository.auth.AuthCodeRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser(username="admin",roles={"ADMIN"})
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authMockController = new AuthController();

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Autowired
    private UserRepository userRepository;



    @Test
    void testRegisterUserAndLogin(){
        authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new AuthRequest("mati@gmail.com", "Prueba123"));
        Assert.assertEquals("mati@gmail.com", response.getUser().getEmail());
        Assert.assertEquals("ROLE_USER", response.getUser().getRole().getName());
    }

    @Test
    void testLoginAndLogout(){
        authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
        LoginResponse response = authController.authenticateUser(new AuthRequest("mati@gmail.com", "Prueba123"));
        authController.logout("Bearer " + response.getAccessToken());
        AccessDeniedException e = Assertions.assertThrows(AccessDeniedException.class, () -> {
            authController.deleteUser(2L);
        });
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
    void testLoginInternalErrror() throws UserNotFoundException {
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.when(authService.login(request)).thenThrow(UserNotFoundException.class);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authMockController.authenticateUser(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
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
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource({
            "'','prueba123'",
            ",'prueba123'",
            "'123456',",
            "'123456',''"
    })
    void testInvalidRequestResetPassword(String code, String pass) {
        ResetPasswordRequest request = new ResetPasswordRequest(code, pass);
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () -> {
            authController.resetPassword(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(), e.getReason());
    }

    @Test
    void testResetPasswordOldCode(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -10);

        createCode(calendar.getTime());
        ResetPasswordRequest request = new ResetPasswordRequest("123456", "123456");
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () -> {
            authController.resetPassword(request);
        });
        Assert.assertEquals(ResponseMessage.CODE_EXPIRED.getMessage(), e.getReason());
    }

    @Test
    void testHappyPath(){
        createCode(Calendar.getInstance().getTime());
        ResetPasswordRequest request = new ResetPasswordRequest("123456", "123456");
        assertDoesNotThrow(()->authController.resetPassword(request));
    }

    @Test
    void testGetRoles(){
        List<RoleResponse> roles = authController.getRoles();
        Assert.assertEquals(2L, roles.size());
        Assert.assertEquals("ROLE_USER", roles.get(0).getName());
        Assert.assertEquals("ROLE_ADMIN", roles.get(1).getName());
    }

    @Test
    void getUsers(){
        createUser();
        List<UserResponse> users = authController.getUsers();
        Assert.assertEquals(1L, users.size());
        Assert.assertEquals("Matias", users.get(0).getName());
        Assert.assertEquals("Reimondo", users.get(0).getSurname());
    }

    @Test
    void getUser(){
        createUser();
        UserResponse user= authController.getUser(1L);
        Assert.assertEquals("mati@gmail.com", user.getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.getRole().getName());
    }

    @Test
    void testCreateUser(){
        UserResponse response = authController.createUser(createUserRequest("mati"));
        Optional<User> user = userRepository.findById(1L);

        Assert.assertEquals("mati@gmail.com", user.get().getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.get().getRole().name());
        Assert.assertEquals("mati@gmail.com", response.getEmail());
    }

    @Test
    void testCreaterUserInternalErrror() {
        UserRequest request = createUserRequest("mati");
        Mockito.when(authService.createUser(request)).thenThrow(RuntimeException.class);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authMockController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    @Test
    void testCreateDuplicateUser(){
        UserRequest request = createUserRequest("mati");
        authController.createUser(request);
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () -> {
            authController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.DUPLICATE_EMAIL.getMessage(), e.getReason());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'mati.com'",
            "'cac@'",
            "cac@.com",
            "@gmail.com",
            "null",
            "''"
    }, nullValues = {"null"})
    void testCreaterUserInvalidEmail(String email) {
        UserRequest request = createUserRequest("mati");
        request.setEmail(email);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_USER.getMessage(),e.getReason());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "''",
            "null"
    }, nullValues = {"null"})
    void testCreaterUserInvalidName(String name) {
        UserRequest request = createUserRequest("mati");
        request.setName(name);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_USER.getMessage(),e.getReason());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "''",
            "null"
    }, nullValues = {"null"})
    void testCreateUserInvalidSurname(String surname) {
        UserRequest request = createUserRequest("mati");
        request.setSurname(surname);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_USER.getMessage(),e.getReason());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "''",
            "null"
    }, nullValues = {"null"})
    void testCreateUserInvalidPassword(String pass) {
        UserRequest request = createUserRequest("mati");
        request.setPassword(pass);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.createUser(request);
        });
        Assert.assertEquals(ResponseMessage.INVALID_USER.getMessage(),e.getReason());
    }

    @Test
    void testUpdateUser(){
        authController.createUser(createUserRequest("mati"));
        authController.updateUser(1L, createUserRequest("lucas"));

        Optional<User> user = userRepository.findById(1L);

        Assert.assertEquals("lucas@gmail.com", user.get().getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.get().getRole().name());
        Assert.assertEquals("lucas", user.get().getName());
    }

    @Test
    void testUpdateNotExistentUser() {
        UserRequest request = createUserRequest("mati");
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.updateUser(1L,request);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testDeleteUser(){
        authController.createUser(createUserRequest("mati"));
        authController.deleteUser(1L);

        Optional<User> user = userRepository.findById(1L);

        Assert.assertFalse(user.isPresent());
    }

    @Test
    void testDeleteNotExistentUser() {
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.deleteUser(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }



    @Test
    void getUserNotExist(){
        ResponseStatusException e = Assert.assertThrows(ResponseStatusException.class, () ->{
            authController.getUser(1L);
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    private AuthCode createCode(Date date){
        authController.registerUser(new AuthRequest("mati@gmail.com","Prueba123"));
        Optional<User> user = userRepository.findByEmail("mati@gmail.com");
        AuthCode authCode = new AuthCode();
        authCode.setCode("123456");
        authCode.setUser(user.get());
        authCode.setRefreshDate(date);
        authCodeRepository.save(authCode);
        return authCode;
    }

    private User createUser(){
        User user = new User();
        user.setName("Matias");
        user.setSurname("Reimondo");
        user.setEmail("mati@gmail.com");
        user.setPassword("Prueba123");
        user.setRole(ERole.ROLE_ADMIN);
        user.setDescription("Es un usuario de prueba");
        userRepository.save(user);
        return user;
    }

    private UserRequest createUserRequest(String placeholder){
        UserRequest user = new UserRequest();
        user.setName(placeholder);
        user.setSurname("Reimondo");
        user.setEmail(placeholder+"@gmail.com");
        user.setPassword("Prueba123");
        user.setRole(ERole.ROLE_ADMIN.name());
        user.setDescription("Es un usuario de prueba");
        return user;
    }

}
