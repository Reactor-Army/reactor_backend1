package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.exception.EmailAlreadyExistException;
import fiuba.tpp.reactorapp.model.auth.exception.InvalidRegisterException;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.response.ResponseMessage;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ResponseMessage.EMAIL_NOT_FOUND.getMessage(), e);
        }
    }

    private void validateAuthRequest(AuthRequest request) throws InvalidRegisterException {
        if(request.getEmail() == null || request.getEmail().isEmpty() || !request.getEmail().contains("@")) throw new InvalidRegisterException();
    }

}
