package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.exception.*;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.model.response.auth.RoleResponse;
import fiuba.tpp.reactorapp.model.response.auth.UserResponse;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody AuthRequest registerRequest) {
        try{
            validateAuthRequest(registerRequest);
            return authService.register(registerRequest);
        } catch (EmailAlreadyExistException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL.getMessage(), e);
        } catch (InvalidRegisterException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_REGISTER.getMessage(), e);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    @PostMapping("/reset/password/code")
    public void generateCodeResetPassword(@RequestBody AuthRequest request){
        try{
            validateAuthRequest(request);
            authService.resetPasswordGenerateCode(request);
        } catch (InvalidRegisterException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INVALID_REGISTER.getMessage(), e);
        } catch (Exception ignored) {
            //Se ignora y se devuelve 200
        }
    }

    @PostMapping("/reset/password")
    public void resetPassword(@RequestBody ResetPasswordRequest request){
        try{
            validateResetPasswordRequest(request);
            authService.resetPassword(request);
        } catch (InvalidResetPasswordException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        } catch (CodeExpiredException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ResponseMessage.CODE_EXPIRED.getMessage(), e);
        } catch (CodeNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.INTERNAL_ERROR.getMessage(), e);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping("/roles")
    public List<RoleResponse> getRoles(){
        return authService.getRoles();
    }

    private void validateAuthRequest(AuthRequest request) throws InvalidRegisterException {
        if(request.getEmail() == null || request.getEmail().isEmpty() || !request.getEmail().contains("@")) throw new InvalidRegisterException();
    }

    private void validateResetPasswordRequest(ResetPasswordRequest request) throws InvalidResetPasswordException {
        if(request.getCode() == null || request.getCode().isEmpty())  throw new InvalidResetPasswordException();
        if(request.getPassword() == null || request.getPassword().isEmpty()) throw new InvalidResetPasswordException();
    }

}
