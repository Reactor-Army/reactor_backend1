package fiuba.tpp.reactorapp.service.auth;

import fiuba.tpp.reactorapp.entities.auth.AuthCode;
import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.CodeExpiredException;
import fiuba.tpp.reactorapp.model.auth.exception.CodeNotFoundException;
import fiuba.tpp.reactorapp.model.auth.exception.EmailAlreadyExistException;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.ResetPasswordRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.response.auth.RoleResponse;
import fiuba.tpp.reactorapp.model.response.auth.UserResponse;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthCodeService authCodeService;

    private static final int CODE_DURATION = 10;

    public RegisterResponse register(AuthRequest request) throws EmailAlreadyExistException {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new EmailAlreadyExistException();
        }

        User user = new User(request.getEmail(),encoder.encode(request.getPassword()));

        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);

        return new RegisterResponse(user);

    }

    public LoginResponse login(AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(jwt, userDetails.getId(), userDetails.getEmail(), roles);

    }

    public void resetPasswordGenerateCode(AuthRequest request) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isPresent()){
           authCodeService.generateAuthCode(user.get());
        }else{
            throw new UserNotFoundException();
        }
    }

    public List<RoleResponse> getRoles(){
        List<RoleResponse> roles = new ArrayList<>();
        for (ERole role :ERole.values()) {
            roles.add(new RoleResponse(role.name(),role.getDescription()));
        }
        return roles;

    }

    public List<UserResponse> getUsers(){
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: userRepository.findAll()) {
            UserResponse response = new UserResponse(user);
            userResponses.add(response);
        }
        return userResponses;
    }

    public UserResponse getUser(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new UserResponse(user.get());
        }
        throw new UserNotFoundException();
    }

    public void resetPassword(ResetPasswordRequest request) throws CodeExpiredException, CodeNotFoundException {
        AuthCode authCode = authCodeService.getAuthCode(request.getCode());
        validateCodeExpiration(authCode.getRefreshDate());
        authCode.getUser().setPassword(encoder.encode(request.getPassword()));
        userRepository.save(authCode.getUser());
    }

    private void validateCodeExpiration(Date codeDate) throws CodeExpiredException {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.setTime(codeDate);
        calendar.add(Calendar.MINUTE, CODE_DURATION);
        Date validTime = calendar.getTime();
        if(!codeDate.before(now) || !validTime.after(now)){
            throw new CodeExpiredException();
        }
    }
}
