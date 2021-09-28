package fiuba.tpp.reactorapp.model.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleResponse {

    @JsonProperty("nombre")
    private String name;

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
}
