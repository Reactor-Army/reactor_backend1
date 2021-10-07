package fiuba.tpp.reactorapp.model.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleResponse {

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("nombreVerbose")
    private String rolename;

    @JsonProperty("descripcion")
    private String description;

    public RoleResponse(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public RoleResponse(String name, String rolename, String description) {
        this.name = name;
        this.rolename = rolename;
        this.description = description;
    }
}
