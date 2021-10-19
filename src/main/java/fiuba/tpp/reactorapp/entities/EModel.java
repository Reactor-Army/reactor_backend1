package fiuba.tpp.reactorapp.entities;

public enum EModel {
    THOMAS("Modelo de Thomas"),
    YOON_NELSON("Modelo de Yoon-Nelson"),
    ADAMS_BOHART("Modelo de Adams-Bohart");

    private final String modelName;

    EModel(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
}
