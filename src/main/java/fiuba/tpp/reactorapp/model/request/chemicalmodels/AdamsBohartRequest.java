package fiuba.tpp.reactorapp.model.request.chemicalmodels;

import org.springframework.web.multipart.MultipartFile;

public class AdamsBohartRequest {

    private MultipartFile observaciones;

    //F
    private Double caudalVolumetrico;

    //Co
    private Double concentracionInicial;

    //Uo
    private Double velocidadLineal;

    //Z
    private Double alturaLechoReactor;


    public AdamsBohartRequest(MultipartFile observaciones, Double caudalVolumetrico, Double concentracionInicial, Double velocidadLineal, Double alturaLechoReactor) {
        this.observaciones = observaciones;
        this.caudalVolumetrico = caudalVolumetrico;
        this.concentracionInicial = concentracionInicial;
        this.velocidadLineal = velocidadLineal;
        this.alturaLechoReactor = alturaLechoReactor;
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

    public Double getVelocidadLineal() {
        return velocidadLineal;
    }

    public void setVelocidadLineal(Double velocidadLineal) {
        this.velocidadLineal = velocidadLineal;
    }

    public Double getAlturaLechoReactor() {
        return alturaLechoReactor;
    }

    public void setAlturaLechoReactor(Double alturaLechoReactor) {
        this.alturaLechoReactor = alturaLechoReactor;
    }
}
