package fiuba.tpp.reactorapp.model.dto;

import fiuba.tpp.reactorapp.model.request.chemicalmodels.YoonNelsonRequest;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;

public class BreakCurvesYoonNelsonDTO {

    private YoonNelsonRequest request;

    private YoonNelsonResponse response;

    public BreakCurvesYoonNelsonDTO() {
    }

    public BreakCurvesYoonNelsonDTO(YoonNelsonRequest request, YoonNelsonResponse response) {
        this.request = request;
        this.response = response;
    }

    public YoonNelsonRequest getRequest() {
        return request;
    }

    public void setRequest(YoonNelsonRequest request) {
        this.request = request;
    }

    public YoonNelsonResponse getResponse() {
        return response;
    }

    public void setResponse(YoonNelsonResponse response) {
        this.response = response;
    }
}
