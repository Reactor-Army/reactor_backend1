package fiuba.tpp.reactorapp.service.auth;

import fiuba.tpp.reactorapp.entities.auth.ERole;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.model.auth.exception.EmailAlreadyExistException;
import fiuba.tpp.reactorapp.model.auth.exception.UserNotFoundException;
import fiuba.tpp.reactorapp.model.auth.request.AuthRequest;
import fiuba.tpp.reactorapp.model.auth.request.UserRequest;
import fiuba.tpp.reactorapp.model.auth.response.LoginResponse;
import fiuba.tpp.reactorapp.model.auth.response.RegisterResponse;
import fiuba.tpp.reactorapp.model.auth.response.RoleResponse;
import fiuba.tpp.reactorapp.model.auth.response.UserResponse;
import fiuba.tpp.reactorapp.model.exception.SameUserException;
import fiuba.tpp.reactorapp.repository.auth.TokenRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.security.jwt.JwtUtils;
import fiuba.tpp.reactorapp.security.services.UserDetailsImpl;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    TokenRepository tokenRepository;

    public RegisterResponse register(AuthRequest request) throws EmailAlreadyExistException {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new EmailAlreadyExistException();
        }

        User user = new User(request.getEmail(),encoder.encode(request.getPassword()));
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);

        return new RegisterResponse(user);
    }

    public LoginResponse login(AuthRequest request, String device) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        if(!user.isPresent()) throw new UserNotFoundException();

        String jwt = jwtUtils.generateJwtToken(authentication, user.get(), device);


        return new LoginResponse(jwt, new UserResponse(updateLastLogin(user.get())));
    }

    private User updateLastLogin(User user){
        user.setLastLogin(Calendar.getInstance().getTime());
        userRepository.save(user);
        return user;
    }

    public void logout(String authHeader, String device){
        jwtUtils.invalidateJwtToken(jwtUtils.parseJwtHeader(authHeader), device);
    }

    public List<RoleResponse> getRoles(){
        List<RoleResponse> roles = new ArrayList<>();
        for (ERole role :ERole.values()) {
            roles.add(new RoleResponse(role.name(),role.getRoleName(),role.getDescription()));
        }
        return roles;

    }

    public List<UserResponse> getUsers(){
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: userRepository.getAll()) {
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

    public UserResponse createUser(UserRequest request) throws EmailAlreadyExistException {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new EmailAlreadyExistException();
        }
        User user = new User(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(EnumUtils.getEnum(ERole.class,request.getRole()));

        userRepository.save(user);

        return new UserResponse(user);
    }

    public UserResponse updateUser(Long id,UserRequest request) throws EmailAlreadyExistException, UserNotFoundException {
        if (Boolean.TRUE.equals(userRepository.findByEmailAndIdNot(request.getEmail(),id).isPresent())) {
            throw new EmailAlreadyExistException();
        }
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User userUpdated = user.get().update(request);
            if(isValidPassword(request.getPassword())){
                userUpdated.setPassword(encoder.encode(request.getPassword()));
            }
            userUpdated.setRole(EnumUtils.getEnum(ERole.class,request.getRole()));
            userRepository.save(userUpdated);
            return new UserResponse(userUpdated);
        }
        throw new UserNotFoundException();
    }

    private boolean isValidPassword(String password){
        return password != null && !password.isEmpty();
    }

    @Transactional
    public void deleteUser(Long id, String token) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            if(isSameUser(jwtUtils.getUserNameFromJwtToken(token),user.get().getEmail())) throw new SameUserException();
            tokenRepository.deleteAllByUser(user.get());
            userRepository.delete(user.get());
        }else{
            throw new UserNotFoundException();
        }
    }

    private boolean isSameUser(String emailUser, String emailDeleted){
        return emailUser.equalsIgnoreCase(emailDeleted);
    }


}
