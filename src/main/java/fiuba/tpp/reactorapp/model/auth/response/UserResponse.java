package fiuba.tpp.reactorapp.model.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.auth.User;


public class UserResponse {

    private Long id;

    private String email;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("apellido")
    private String surname;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("rol")
    private RoleResponse role;

    public UserResponse(User user) {
        copyData(user);
    }

    private void copyData(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.description = user.getDescription();
        this.role = new RoleResponse(user.getRole().name(),user.getRole().getDescription());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }
}
