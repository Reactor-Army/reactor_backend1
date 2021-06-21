package fiuba.tpp.reactorapp.model.response;

public enum ResponseMessage {
    DUPLICATE_ADSORBATE("Ya existe otro adsorbato con ese nombre IUPAC"),
    INVALID_ADSORBATE("Los adsorbatos deben tener un nombre y un nombre IUPAC"),
    ADSORBATE_NOT_FOUND("El adsorbato no existe"),
    ADSORBATE_INVALID_REQUEST("Es necesario el ID del adsorbato y el nombre IUPAC no puede ser nulo"),
    DUPLICATE_ADSORBENT("Ya existe un adsorbente con ese nombre y ese tamaño de particula"),
    INVALID_ADSORBENT("Los adsorbentes deben tener un nombre"),
    ADSORBENT_NOT_FOUND("El Adsorbente no existe"),
    PROCESS_NOT_FOUND("El proceso no existe"),
    INVALID_PROCESS("Adsorbato,Adsorbente o reactor invalido"),
    PROCESS_INVALID_REQUEST("El proceso debe estar conformado de un adsorbato o un adsorbente"),
    INVALID_PROCESS_CREATE("Adsorbente o Adsorbato invalidos"),
    DUPLICATE_EMAIL("El email ya existe en el sistema"),
    INVALID_REGISTER("El email es invalido o no cumple con un formato correcto"),
    INTERNAL_ERROR("Ocurrio un error inesperado");

    private final String message;

    ResponseMessage(String message){
        this.message= message;
    }

    public String getMessage() {
        return message;
    }
}
