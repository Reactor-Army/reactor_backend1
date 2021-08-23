package fiuba.tpp.reactorapp.model.request.chemicalmodels;

import org.springframework.web.multipart.MultipartFile;

public class YoonNelsonRequest {

    private MultipartFile observaciones;

    //F
    private Double caudalVolumetrico;

    public YoonNelsonRequest(MultipartFile observaciones, Double caudalVolumetrico) {
        this.observaciones = observaciones;
        this.caudalVolumetrico = caudalVolumetrico;
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
}
