package fiuba.tpp.reactorapp.entities.auth;

public enum ERole {
    ROLE_USER("Usuario de laboratorio, puede ver toda la información del sistema"),
    ROLE_ADMIN("Usuario administrador, puede editar la información del sistema");

    private final String description;

    ERole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
