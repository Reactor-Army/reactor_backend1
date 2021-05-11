package fiuba.tpp.reactorapp.model.auth.response;

import fiuba.tpp.reactorapp.entities.auth.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterResponse {

    private Long id;
    private String email;
    private List<String> roles;

    public RegisterResponse(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public RegisterResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = new ArrayList<>();
        roles.add(user.getRole().name());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
