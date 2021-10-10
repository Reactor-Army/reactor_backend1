package fiuba.tpp.reactorapp.repository.auth;

import fiuba.tpp.reactorapp.entities.auth.AuthCode;
import fiuba.tpp.reactorapp.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {

    Optional<AuthCode> findByUser(User user);

    Optional<AuthCode> findTopByCodeOrderByRefreshDate(String code);

    void deleteAllByUser(User user);
}
