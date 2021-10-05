package fiuba.tpp.reactorapp.entities.auth;

public enum ERole {
    ROLE_USER("Usuario","Usuario de laboratorio, puede ver toda la información del sistema"),
    ROLE_ADMIN("Administrador","Usuario administrador, puede editar la información del sistema");

    private final String roleName;
    private final String description;


    ERole(String roleName,String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRoleName() {
        return roleName;
    }
}
