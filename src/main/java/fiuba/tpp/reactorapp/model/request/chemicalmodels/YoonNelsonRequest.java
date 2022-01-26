package fiuba.tpp.reactorapp.model.request.chemicalmodels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public class YoonNelsonRequest {

    @JsonIgnore
    private MultipartFile observaciones;

    //F
    private Double caudalVolumetrico;

    //Co
    private Double concentracionInicial;

    public YoonNelsonRequest() {
    }

    public YoonNelsonRequest(MultipartFile observaciones, Double caudalVolumetrico, Double concentracionInicial) {
        this.observaciones = observaciones;
        this.caudalVolumetrico = caudalVolumetrico;
        this.concentracionInicial = concentracionInicial;
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
}

