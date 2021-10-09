package fiuba.tpp.reactorapp.repository.auth;

import fiuba.tpp.reactorapp.entities.auth.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> getAll();
}
