package fiuba.tpp.reactorapp.controller;

import fiuba.tpp.reactorapp.model.auth.exception.EmailAlreadyExistException;
import fiuba.tpp.reactorapp.model.auth.exception.InvalidRegisterException;
import fiuba.tpp.reactorapp.model.auth.request.LoginRequest;
import fiuba.tpp.reactorapp.model.auth.request.RegisterRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest) {
        try{
            validateRegisterRequest(registerRequest);
            return authService.register(registerRequest);
        } catch (EmailAlreadyExistException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El email ya existe en el sistema", e);
        } catch (InvalidRegisterException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El email es invalido o no cumple con un formato correcto", e);
        }
    }

    private void validateRegisterRequest(RegisterRequest request) throws InvalidRegisterException {
        if(request.getEmail() == null || request.getEmail().isEmpty() || !request.getEmail().contains("@")) throw new InvalidRegisterException();
    }

}
