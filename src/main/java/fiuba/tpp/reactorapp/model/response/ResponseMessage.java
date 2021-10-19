package fiuba.tpp.reactorapp.model.response;

public enum ResponseMessage {
    DUPLICATE_ADSORBATE("Ya existe otro adsorbato con ese nombre IUPAC"),
    INVALID_ADSORBATE("Los adsorbatos deben tener un nombre y un nombre IUPAC"),
    ADSORBATE_NOT_FOUND("El adsorbato no existe"),
    ADSORBATE_INVALID_REQUEST("Es necesario el ID del adsorbato y el nombre IUPAC no puede ser nulo"),
    DUPLICATE_ADSORBENT("Ya existe un adsorbente con ese nombre y ese tamaño de partícula"),
    INVALID_ADSORBENT("Los adsorbentes deben tener un nombre"),
    ADSORBENT_NOT_FOUND("El Adsorbente no existe"),
    PROCESS_NOT_FOUND("El sistema no existe"),
    INVALID_PROCESS("Adsorbato,Adsorbente o sistema invalido"),
    PROCESS_INVALID_REQUEST("El sistema debe estar conformado de un adsorbato o un adsorbente"),
    INVALID_PROCESS_CREATE("Adsorbente o Adsorbato inválidos"),
    INVALID_REACTION_ORDER("El orden de la reacción debe ser 1 o 2"),
    INVALID_KINETIC_CONSTANT("La constante cinética tiene que ser mayor a 0"),
    INVALID_VOLUME_REQUEST("Se necesitan todos los datos para calcular el volumen del reactor, deben ser positivos"),
    INVALID_KINECT_INFORMATION("El sistema no tiene los datos del modelo cinético"),
    DUPLICATE_EMAIL("El email ya existe en el sistema"),
    INVALID_REGISTER("El email es invalido o no cumple con un formato correcto"),
    FILE_NOT_FOUND("El archivo es invalido o no cumple con el formato correcto"),
    TESIS_NOT_FOUND("La tesis no existe"),
    INVALID_FILE("El archivo debe tener una extensión csv, xls o xlsx"),
    INVALID_TESIS_FILE("El archivo debe tener una extensión pdf, doc o docx"),
    TESIS_FILE_SIZE_EXCEED("El tamaño limite para un archivo son 50 MB"),
    INVALID_HEADER("El archivo debe tener las columnas volumenEfluente y C/C0, los datos de las columnas deben ser numéricos"),
    INVALID_THOMAS("Se requiere el caudal volumétrico, la concentración inicial del adsorbato, la cantidad de sorbente en el reactor y las observaciones"),
    INVALID_NELSON("Se requiere el caudal volumétrico y las observaciones"),
    INVALID_BOHART("Se requiere el caudal volumétrico, la concentración inicial del adsorbato, la velocidad lineal, la altura del lecho del reactor, y las observaciones"),
    INVALID_FIELDS("Recordá que los campos deben ser numéricos"),
    CODE_EXPIRED("Tu código ya ha expirado, intentá reiniciar la contraseña nuevamente"),
    INVALID_USER("Para crear un usuario se requiere un nombre y apellido, un email válido, unas password y uno de los roles permitidos"),
    BAD_CREDENTIALS("Credenciales inválidas"),
    UNAUTHORIZED("Para realizar esta operación debes ser administrador del sistema"),
    INVALID_TESIS_REQUEST("Los trabajos deben tener un titulo y un autor"),
    SAME_USER_ERROR("Un administrador no puede borrarse a si mismo"),
    DATA_NOT_FOUND("Los datos a los que intenta acceder no existen"),
    INTERNAL_ERROR("Ocurrió un error inesperado");

    private final String message;

    ResponseMessage(String message){
        this.message= message;
    }

    public String getMessage() {
        return message;
    }
}
