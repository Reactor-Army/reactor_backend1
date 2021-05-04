package fiuba.tpp.reactorapp.model.response;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbente;
import fiuba.tpp.reactorapp.entities.Reactor;

public class ReactorResponse {

    private Long id;

    private Adsorbate adsorbate;

    private Adsorbente adsorbente;

    private Float qmax;

    private Float tiempoEquilibrio;

    private Float temperatura;

    private Float phinicial;

    private boolean complejacion;

    private boolean intercambioIonico;

    private boolean reaccionQuimica;

    private String observacion;

    private String fuente;

    public ReactorResponse() {
    }

    public ReactorResponse(Reactor reactor) {
        this.id = reactor.getId();
        this.adsorbate = reactor.getAdsorbate();
        this.adsorbente = reactor.getAdsorbente();
        this.qmax = reactor.getQmax();
        this.tiempoEquilibrio = reactor.getTiempoEquilibrio();
        this.temperatura = reactor.getTemperatura();
        this.phinicial = reactor.getPhinicial();
        this.complejacion = reactor.isComplejacion();
        this.intercambioIonico = reactor.isIntercambioIonico();
        this.reaccionQuimica = reactor.isReaccionQuimica();
        this.observacion = reactor.getObservacion();
        this.fuente = reactor.getFuente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adsorbate getAdsorbato() {
        return adsorbate;
    }

    public void setAdsorbato(Adsorbate adsorbate) {
        this.adsorbate = adsorbate;
    }

    public Adsorbente getAdsorbente() {
        return adsorbente;
    }

    public void setAdsorbente(Adsorbente adsorbente) {
        this.adsorbente = adsorbente;
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
}
