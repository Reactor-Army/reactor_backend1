package fiuba.tpp.reactorapp.model.request;

import org.springframework.web.multipart.MultipartFile;

/**
 * ln(Co/C -1) = (Kth * Qo * W)/ F - (Kth * Co)/F Vef
 *
 * */
public class ThomasRequest {

    private MultipartFile observaciones;

    //F
    private Double caudalVolumetrico;

    //Co
    private Double concentracionInicial;

    //W
    private Double sorbenteReactor;

    public ThomasRequest(MultipartFile observaciones) {
        this.observaciones = observaciones;
    }

    public ThomasRequest() {
    }

    public ThomasRequest(MultipartFile observaciones, Double caudalVolumetrico, Double concentracionInicial, Double sorbenteReactor) {
        this.observaciones = observaciones;
        this.caudalVolumetrico = caudalVolumetrico;
        this.concentracionInicial = concentracionInicial;
        this.sorbenteReactor = sorbenteReactor;
    }

    public MultipartFile getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(MultipartFile observaciones) {
        this.observaciones = observaciones;
    }

    public Double getCaudalVolumetrico() {
        return caudalVolumetrico;
    }

    public void setCaudalVolumetrico(Double caudalVolumetrico) {
        this.caudalVolumetrico = caudalVolumetrico;
    }

    public Double getConcentracionInicial() {
        return concentracionInicial;
    }

    public void setConcentracionInicial(Double concentracionInicial) {
        this.concentracionInicial = concentracionInicial;
    }

    public Double getSorbenteReactor() {
        return sorbenteReactor;
    }

    public void setSorbenteReactor(Double sorbenteReactor) {
        this.sorbenteReactor = sorbenteReactor;
    }
}
