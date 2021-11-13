package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorQRequest {

    @JsonProperty("idLineaBase")
    private Long baselineId;

    @JsonProperty("idCurva")
    private Long curveId;

    public ReactorQRequest(Long baselineId, Long curveId) {
        this.baselineId = baselineId;
        this.curveId = curveId;
    }

    public Long getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(Long baselineId) {
        this.baselineId = baselineId;
    }

    public Long getCurveId() {
        return curveId;
    }

    public void setCurveId(Long curveId) {
        this.curveId = curveId;
    }
}
