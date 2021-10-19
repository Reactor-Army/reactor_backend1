package fiuba.tpp.reactorapp.model.dto;

import fiuba.tpp.reactorapp.model.request.chemicalmodels.AdamsBohartRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;

public class BreakCurvesAdamsDTO {

    private AdamsBohartRequest request;

    private AdamsBohartResponse response;

    public BreakCurvesAdamsDTO() {
    }

    public BreakCurvesAdamsDTO(AdamsBohartRequest request, AdamsBohartResponse response) {
        this.request = request;
        this.response = response;
    }

    public AdamsBohartRequest getRequest() {
        return request;
    }

    public void setRequest(AdamsBohartRequest request) {
        this.request = request;
    }

    public AdamsBohartResponse getResponse() {
        return response;
    }

    public void setResponse(AdamsBohartResponse response) {
        this.response = response;
    }
}
