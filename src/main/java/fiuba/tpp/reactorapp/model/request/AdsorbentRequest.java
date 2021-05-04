package fiuba.tpp.reactorapp.model.request;

public class AdsorbentRequest {

    private Long id;

    private String nombre;

    private String particulaT;

    private Float sBet;

    private Float vBet;

    private Float pHCargaCero;

    public AdsorbentRequest() {
    }

    public AdsorbentRequest(String nombre, String particulaT, Float sBet, Float vBet, Float pHCargaCero) {
        this.nombre = nombre;
        this.particulaT = particulaT;
        this.sBet = sBet;
        this.vBet = vBet;
        this.pHCargaCero = pHCargaCero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getParticulaT() {
        return particulaT;
    }

    public void setParticulaT(String particulaT) {
        this.particulaT = particulaT;
    }

    public Float getsBet() {
        return sBet;
    }

    public void setsBet(Float sBet) {
        this.sBet = sBet;
    }

    public Float getvBet() {
        return vBet;
    }

    public void setvBet(Float vBet) {
        this.vBet = vBet;
    }

    public Float getpHCargaCero() {
        return pHCargaCero;
    }

    public void setpHCargaCero(Float pHCargaCero) {
        this.pHCargaCero = pHCargaCero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
