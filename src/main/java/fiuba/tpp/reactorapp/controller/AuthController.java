package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.model.auth.exception.*;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.request.UserRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.exception.SameUserException;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.auth.response.RoleResponse;
import fiuba.tpp.reactorapp.model.auth.response.UserResponse;
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private static final Integer EMAIL_MAX_LENGTH = 64;

    @Autowired
    AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody AuthRequest authRequest, @RequestHeader(value = "User-Agent") String userAgent) {
        try {
            return authService.login(authRequest, userAgent);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authHeader,@RequestHeader(value = "User-Agent") String userAgent) {
        authService.logout(authHeader, userAgent);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getUsers(){
        return authService.getUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id){
        try{
            return authService.getUser(id);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    public List<RoleResponse> getRoles(){
        return authService.getRoles();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users")
    public UserResponse createUser(@RequestBody UserRequest userRequest){
        try{
            validateUserRequest(userRequest);
            return authService.createUser(userRequest);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_USER.getMessage(), e);
        }catch (EmailAlreadyExistException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{id}")
    public UserResponse updateUser(@PathVariable Long id,@RequestBody UserRequest userRequest){
        try{
            validateUpdateUserRequest(userRequest);
            return authService.updateUser(id,userRequest);
        } catch (InvalidUserException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_USER.getMessage(), e);
        }catch (EmailAlreadyExistException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL.getMessage(), e);
        }catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id, @RequestHeader(name ="Authorization", required = true) String authHeader){
        try{
            authService.deleteUser(id, jwtUtils.parseJwtHeader(authHeader));
        }catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }catch (SameUserException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.SAME_USER_ERROR.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }


    /**
     * Validamos que los campos email nombre apellido password no sean nulos
     * Ademas el rol debe existir entre los roles posibles
     * @param request
     * @throws InvalidUserException
     */
    private void validateUserRequest(UserRequest request) throws InvalidUserException {
        validateUserData(request);
        if(request.getPassword() == null || request.getPassword().isEmpty()) throw new InvalidUserException();
    }

    /**
     * Validamos que los campos email nombre apellido no sean nulos
     * Ademas el rol debe existir entre los roles posibles
     * @param request
     * @throws InvalidUserException
     */
    private void validateUpdateUserRequest(UserRequest request) throws InvalidUserException {
        validateUserData(request);
    }

    private void validateUserData(UserRequest request) throws InvalidUserException {
        if(request.getEmail() == null || request.getEmail().isEmpty() || !isValidEmail(request.getEmail())) throw new InvalidUserException();
        if(request.getName() == null || request.getName().isEmpty()) throw new InvalidUserException();
        if(request.getSurname() == null || request.getSurname().isEmpty()) throw new InvalidUserException();
        if(!EnumUtils.isValidEnum(ERole.class, request.getRole())) throw new InvalidUserException();
    }

    private boolean isValidEmail(String email){
        if(email.length() > EMAIL_MAX_LENGTH) return false;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
