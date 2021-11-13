package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReactorQResponse {

    @JsonProperty("lineaBase")
    private BreakCurvesDataResponse baseline;

    @JsonProperty("curva")
    private BreakCurvesDataResponse curve;

    @JsonProperty("areaLineaBase")
    private double baselineArea;

    @JsonProperty("areaCurva")
    private double curveArea;

    @JsonProperty("reactorQ")
    private double reactorQ;

    public ReactorQResponse(BreakCurvesDataResponse baseline, BreakCurvesDataResponse curve, double baselineArea, double curveArea, double reactorQ) {
        this.baseline = baseline;
        this.curve = curve;
        this.baselineArea = baselineArea;
        this.curveArea = curveArea;
        this.reactorQ = reactorQ;
    }

    public BreakCurvesDataResponse getBaseline() {
        return baseline;
    }

    public void setBaseline(BreakCurvesDataResponse baseline) {
        this.baseline = baseline;
    }

    public BreakCurvesDataResponse getCurve() {
        return curve;
    }

    public void setCurve(BreakCurvesDataResponse curve) {
        this.curve = curve;
    }

    public double getBaselineArea() {
        return baselineArea;
    }

    public void setBaselineArea(double baselineArea) {
        this.baselineArea = baselineArea;
    }

    public double getCurveArea() {
        return curveArea;
    }

    public void setCurveArea(double curveArea) {
        this.curveArea = curveArea;
    }

    public double getReactorQ() {
        return reactorQ;
    }

    public void setReactorQ(double reactorQ) {
        this.reactorQ = reactorQ;
    }
}
