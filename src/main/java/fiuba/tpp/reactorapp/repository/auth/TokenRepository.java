package fiuba.tpp.reactorapp.repository.auth;

import fiuba.tpp.reactorapp.entities.auth.Token;
import fiuba.tpp.reactorapp.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByHashToken(String token);

    Optional<Token> findByUserAndDevice(User user, String device);

    void deleteAllByUser(User user);
}

