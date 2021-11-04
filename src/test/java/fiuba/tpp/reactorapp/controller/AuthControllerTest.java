package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.request.UserRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.request.AdsorbentRequest;
import fiuba.tpp.reactorapp.model.response.AdsorbentResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.auth.response.RoleResponse;
import fiuba.tpp.reactorapp.model.auth.response.UserResponse;
import fiuba.tpp.reactorapp.repository.auth.TokenRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@WithMockUser(username="admin",roles={"ADMIN"})
class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authMockController = new AuthController();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AdsorbentController adsorbentController;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void resetDatabase(){
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUserAndLogin(){
        createUserController("mati");
        LoginResponse response = authController.login(new AuthRequest("mati@gmail.com", "Prueba123"),"userAgent");
        Assert.assertEquals("mati@gmail.com", response.getUser().getEmail());
        Assert.assertEquals("Administrador", response.getUser().getRole().getRolename());
        Assert.assertNotNull(response.getUser().getLastLogin());
    }

    @Test
    void testLoginAndLogout(){
        createUser();

        AdsorbentRequest request = new AdsorbentRequest("Prueba", "Prueba", 1f, 1f,1f);
        adsorbentController.createAdsorbent(request);

        LoginResponse response = authController.login(new AuthRequest("mati@gmail.com", "Prueba123"),"userAgent");
        String token = "Bearer " + response.getAccessToken();

        List<AdsorbentResponse> oldList = adsorbentController.getAdsorbents(token, "userAgent");
        authController.logout(token,"userAgent");

        Assert.assertEquals(1L,oldList.size());

        Assert.assertEquals(0L,adsorbentController.getAdsorbents(token,"userAgent").size());
    }

    @Test
    void testInvalidLogin(){
        createUserController("mati");
        AuthRequest request = new AuthRequest("matiTest2", "Prueba123");
        Assert.assertThrows(BadCredentialsException.class, () ->{
            authController.login(request,"userAgent");
        });
    }

    @Test
    void testLoginInternalErrror() throws UserNotFoundException {
        AuthRequest request = new AuthRequest("mati@gmail.com","Prueba123");
        Mockito.when(authService.login(request,"userAgent")).thenThrow(UserNotFoundException.class);
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authMockController.login(request,"userAgent");
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
    }

    @Test
    void testGetRoles(){
        List<RoleResponse> roles = authController.getRoles();
        Assert.assertEquals(2L, roles.size());
        Assert.assertEquals("ROLE_USER", roles.get(0).getName());
        Assert.assertEquals("ROLE_ADMIN", roles.get(1).getName());
    }

    @Test
    void testGetUsers(){
        createUser();
        List<UserResponse> users = authController.getUsers();
        Assert.assertEquals(1L, users.size());
        Assert.assertEquals("Matias", users.get(0).getName());
        Assert.assertEquals("Reimondo", users.get(0).getSurname());
    }

    @Test
    void testGetUsersOrder(){
        authController.createUser(createUserRequest("admin"));
        authController.createUser(createUserRequestNotAdmin("lucas"));
        authController.createUser(createUserRequestNotAdmin("log"));
        authController.login(new AuthRequest("log@gmail.com", "Prueba123"),"userAgent");
        authController.login(new AuthRequest("admin@gmail.com", "Prueba123"),"userAgent");
        List<UserResponse> users = authController.getUsers();
        Assert.assertEquals(3L, users.size());
        Assert.assertEquals("ROLE_ADMIN", users.get(0).getRole().getName());
        Assert.assertEquals("log@gmail.com", users.get(1).getEmail());
        Assert.assertEquals("lucas@gmail.com", users.get(2).getEmail());
    }

    @Test
    void getUser(){
        User userCreated = createUser();
        UserResponse user= authController.getUser(userCreated.getId());
        Assert.assertEquals("mati@gmail.com", user.getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.getRole().getName());
        Assert.assertEquals("Administrador", user.getRole().getRolename());
    }

    @Test
    void testCreateUser(){
        UserResponse response = authController.createUser(createUserRequest("mati"));
        Optional<User> user = userRepository.findById(response.getId());

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
        UserResponse response = authController.createUser(createUserRequest("mati"));
        authController.updateUser(response.getId(), createUserRequest("lucas"));

        Optional<User> user = userRepository.findById(response.getId());

        Assert.assertEquals("lucas@gmail.com", user.get().getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.get().getRole().name());
        Assert.assertEquals("lucas", user.get().getName());
    }

    @Test
    void testUpdateUserWithoutPassword(){
        UserResponse response = authController.createUser(createUserRequest("mati"));
        authController.updateUser(response.getId(), createUserRequestWithoutPassword("lucas"));

        Optional<User> user = userRepository.findById(response.getId());

        Assert.assertEquals("lucas@gmail.com", user.get().getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.get().getRole().name());
        Assert.assertEquals("lucas", user.get().getName());
    }

    @Test
    void testUpdateUserSameEmail(){
        UserResponse response = authController.createUser(createUserRequest("mati"));
        UserRequest req = createUserRequest("lucas");
        req.setEmail("mati@gmail.com");
        authController.updateUser(response.getId(), req);

        Optional<User> user = userRepository.findById(response.getId());

        Assert.assertEquals("mati@gmail.com", user.get().getEmail());
        Assert.assertEquals("ROLE_ADMIN", user.get().getRole().name());
        Assert.assertEquals("lucas", user.get().getName());
    }

    @Test
    void testUpdateUserSameEmailOther(){
        authController.createUser(createUserRequest("lucas"));
        authController.createUser(createUserRequest("mati"));
        UserRequest req = createUserRequest("lucas");
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.updateUser(2L, req);
        });
        Assert.assertEquals(ResponseMessage.DUPLICATE_EMAIL.getMessage(),e.getReason());
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
        UserResponse response = authController.createUser(createUserRequest("mati"));
        authController.deleteUser(response.getId(), getToken("admin"));

        Optional<User> user = userRepository.findById(response.getId());

        Assert.assertFalse(user.isPresent());
    }

    @Test
    void testDeleteNotExistentUser() {
        String token = getToken("admin2");
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.deleteUser(1L, token );
        });
        Assert.assertEquals(ResponseMessage.INTERNAL_ERROR.getMessage(),e.getReason());
        Assert.assertTrue(e.getStatus().is4xxClientError());
    }

    @Test
    void testDeleteSameUser() {
        UserResponse response = createUserController("admin3");
        LoginResponse login = authController.login(new AuthRequest("admin3@gmail.com", "Prueba123"),"userAgent");
        String token = "Bearer " + login.getAccessToken();
        Long id = response.getId();
        ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {
            authController.deleteUser(id, token);
        });
        Assert.assertEquals(ResponseMessage.SAME_USER_ERROR.getMessage(),e.getReason());
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

    private User createUser(){
        User user = new User();
        user.setName("Matias");
        user.setSurname("Reimondo");
        user.setEmail("mati@gmail.com");
        user.setPassword(encoder.encode("Prueba123"));
        user.setRole(ERole.ROLE_ADMIN);
        user.setDescription("Es un usuario de prueba");
        userRepository.save(user);
        return user;
    }

    private UserRequest createUserRequestNotAdmin(String placeholder){
        UserRequest user = new UserRequest();
        user.setName(placeholder);
        user.setSurname("Reimondo");
        user.setEmail(placeholder+"@gmail.com");
        user.setPassword("Prueba123");
        user.setRole(ERole.ROLE_USER.name());
        user.setDescription("Es un usuario de prueba");
        return user;
    }

    private UserRequest createUserRequestWithoutPassword(String placeholder){
        UserRequest user = new UserRequest();
        user.setName(placeholder);
        user.setSurname("Reimondo");
        user.setEmail(placeholder+"@gmail.com");
        user.setRole(ERole.ROLE_ADMIN.name());
        user.setDescription("Es un usuario de prueba");
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

    private UserResponse createUserController(String placeholder){
        return authController.createUser(createUserRequest(placeholder));
    }

    private String getToken(String name){
        createUserController(name);
        LoginResponse response = authController.login(new AuthRequest(name + "@gmail.com", "Prueba123"),"userAgent");
        return  "Bearer " + response.getAccessToken();
    }


}
