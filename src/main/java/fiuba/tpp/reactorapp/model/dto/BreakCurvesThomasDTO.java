package fiuba.tpp.reactorapp.model.dto;

import fiuba.tpp.reactorapp.model.request.chemicalmodels.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;

public class BreakCurvesThomasDTO {

    private ThomasRequest request;

    private ThomasResponse response;

    public BreakCurvesThomasDTO() {
    }

    public BreakCurvesThomasDTO(ThomasRequest request, ThomasResponse response) {
        this.request = request;
        this.response = response;
    }

    public ThomasRequest getRequest() {
        return request;
    }

    public void setRequest(ThomasRequest request) {
        this.request = request;
    }

    public ThomasResponse getResponse() {
        return response;
    }

    public void setResponse(ThomasResponse response) {
        this.response = response;
    }
}
