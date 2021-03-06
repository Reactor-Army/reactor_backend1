package fiuba.tpp.reactorapp.security.jwt;

import fiuba.tpp.reactorapp.entities.auth.Token;
import fiuba.tpp.reactorapp.entities.auth.User;
import fiuba.tpp.reactorapp.repository.auth.TokenRepository;
import fiuba.tpp.reactorapp.repository.auth.UserRepository;
import fiuba.tpp.reactorapp.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${reactor.app.jwtSecret}")
    private String jwtSecret;

    @Value("${reactor.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static final int TOKEN_PREFIX_LENGTH = 7;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public String generateJwtToken(Authentication authentication, User user, String device) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        saveToken(token, user, device);

        return token;
    }

    public void invalidateJwtToken(String token, String device) {
        Optional<User> user = userRepository.findByEmail(getUserNameFromJwtToken(token));
        if(user.isPresent()){
            Optional<Token> tokenDeleted = tokenRepository.findByUserAndDevice(user.get(), device);
            tokenDeleted.ifPresent(t -> tokenRepository.delete(t));
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken, String device) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            Optional<User> user = userRepository.findByEmail(getUserNameFromJwtToken(authToken));
            if(user.isPresent()){
                Optional<Token> t = tokenRepository.findByUserAndDevice(user.get(), device);
                if(t.isPresent()){
                    return t.get().getHashToken().equals(authToken);
                }
            }
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        return parseJwtHeader(headerAuth);
    }

    public String parseJwtHeader(String headerAuth){
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(TOKEN_PREFIX_LENGTH, headerAuth.length());
        }
        return null;
    }

    public boolean isAnonymous(String token, String device){
        if(token == null || token.isEmpty()) return true;
        String authToken = parseJwtHeader(token);
        if(authToken == null || authToken.isEmpty()) return true;
        return !validateJwtToken(authToken, device);
    }

    private void saveToken(String token, User user, String device){
        Token generated;
        Date date = Calendar.getInstance().getTime();
        Optional<Token> tokenGenerated = tokenRepository.findByUserAndDevice(user,device);
        if(tokenGenerated.isPresent()){
            generated = tokenGenerated.get();
            generated.setHashToken(token);
            generated.setCreatedAt(date);
        }else{
            generated = new Token(user,token, Calendar.getInstance().getTime(), device);
        }
        tokenRepository.save(generated);

    }
}
