package fiuba.tpp.reactorapp.security.jwt;

import fiuba.tpp.reactorapp.entities.auth.Token;
import fiuba.tpp.reactorapp.repository.auth.TokenRepository;
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
    private TokenRepository tokenRepository;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        saveToken(token);

        return token;
    }

    public void invalidateJwtToken(String token) {
        Optional<Token> tokenDeleted = tokenRepository.findByHashToken(token);
        tokenDeleted.ifPresent(t -> tokenRepository.delete(t));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            if(tokenRepository.findByHashToken(authToken).isPresent()){
                return true;
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

    public boolean isAnonymous(String token){
        if(token == null || token.isEmpty()) return true;
        String authToken = parseJwtHeader(token);
        if(authToken == null || authToken.isEmpty()) return true;
        return !validateJwtToken(authToken);
    }

    private void saveToken(String token){
        tokenRepository.save(new Token(token, Calendar.getInstance().getTime()));
    }
}
