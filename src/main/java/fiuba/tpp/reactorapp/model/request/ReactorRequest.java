package fiuba.tpp.reactorapp.model.request;


public class ReactorRequest {

    private Long id;

    private Long idAdsorbato;

    private Long idAdsorbente;

    private Float qmax;

    private Float tiempoEquilibrio;

    private Float temperatura;

    private Float phinicial;

    private boolean complejacion;

    private boolean intercambioIonico;

    private boolean reaccionQuimica;

    private String observacion;

    private String fuente;

    public ReactorRequest() {
    }

    public ReactorRequest(Long idAdsorbato, Long idAdsorbente, Float qmax, Float tiempoEquilibrio, Float temperatura, Float phinicial, boolean complejacion, boolean intercambioIonico, boolean reaccionQuimica, String observacion, String fuente) {
        this.idAdsorbato = idAdsorbato;
        this.idAdsorbente = idAdsorbente;
        this.qmax = qmax;
        this.tiempoEquilibrio = tiempoEquilibrio;
        this.temperatura = temperatura;
        this.phinicial = phinicial;
        this.complejacion = complejacion;
        this.intercambioIonico = intercambioIonico;
        this.reaccionQuimica = reaccionQuimica;
        this.observacion = observacion;
        this.fuente = fuente;
    }

    public Long getIdAdsorbato() {
        return idAdsorbato;
    }

    public void setIdAdsorbato(Long idAdsorbato) {
        this.idAdsorbato = idAdsorbato;
    }

    public Long getIdAdsorbente() {
        return idAdsorbente;
    }

    public void setIdAdsorbente(Long idAdsorbente) {
        this.idAdsorbente = idAdsorbente;
    }

    public Float getQmax() {
        return qmax;
    }

    public void setQmax(Float qmax) {
        this.qmax = qmax;
    }

    public Float getTiempoEquilibrio() {
        return tiempoEquilibrio;
    }

    public void setTiempoEquilibrio(Float tiempoEquilibrio) {
        this.tiempoEquilibrio = tiempoEquilibrio;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getPhinicial() {
        return phinicial;
    }

    public void setPhinicial(Float phinicial) {
        this.phinicial = phinicial;
    }

    public boolean isComplejacion() {
        return complejacion;
    }

    public void setComplejacion(boolean complejacion) {
        this.complejacion = complejacion;
    }

    public boolean isIntercambioIonico() {
        return intercambioIonico;
    }

    public void setIntercambioIonico(boolean intercambioIonico) {
        this.intercambioIonico = intercambioIonico;
    }

    public boolean isReaccionQuimica() {
        return reaccionQuimica;
    }

    public void setReaccionQuimica(boolean reaccionQuimica) {
        this.reaccionQuimica = reaccionQuimica;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
