package fiuba.tpp.reactorapp.model.response;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;

public class ProcessResponse {

    private Long id;

    private Adsorbate adsorbato;

    private Adsorbent adsorbente;

    private Float qmax;

    private Float tiempoEquilibrio;

    private Float temperatura;

    private Float phinicial;

    private boolean complejacion;

    private boolean intercambioIonico;

    private boolean reaccionQuimica;

    private String observacion;

    private String fuente;

    public ProcessResponse() {
    }

    public ProcessResponse(Process process) {
        this.id = process.getId();
        this.adsorbato = process.getAdsorbate();
        this.adsorbente = process.getAdsorbent();
        this.qmax = process.getQmax();
        this.tiempoEquilibrio = process.getTiempoEquilibrio();
        this.temperatura = process.getTemperatura();
        this.phinicial = process.getPhinicial();
        this.complejacion = process.isComplejacion();
        this.intercambioIonico = process.isIntercambioIonico();
        this.reaccionQuimica = process.isReaccionQuimica();
        this.observacion = process.getObservacion();
        this.fuente = process.getFuente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adsorbate getAdsorbato() {
        return adsorbato;
    }

    public void setAdsorbato(Adsorbate adsorbato) {
        this.adsorbato = adsorbato;
    }

    public Adsorbent getAdsorbente() {
        return adsorbente;
    }

    public void setAdsorbente(Adsorbent adsorbente) {
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
