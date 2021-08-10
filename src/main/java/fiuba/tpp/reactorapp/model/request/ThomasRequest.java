package fiuba.tpp.reactorapp.model.request;

import org.springframework.web.multipart.MultipartFile;

public class ThomasRequest {

    private MultipartFile observations;

    public ThomasRequest(MultipartFile observations) {
        this.observations = observations;
    }

    public MultipartFile getObservations() {
        return observations;
    }

    public void setObservations(MultipartFile observations) {
        this.observations = observations;
    }
}
