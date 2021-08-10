package fiuba.tpp.reactorapp.model.math;

/**
 *
 * Resultado de la regresion lineal, basada en la libreria Math de Apache common
 * y = intercept + slope * x
 */
public class RegressionResult {

    private double intercept;

    private double slope;

    public RegressionResult() {
    }

    public RegressionResult(double intercept, double slope) {
        this.intercept = intercept;
        this.slope = slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public double getSlope() {
        return slope;
    }

    public void setIntercept(double intercept) {
        this.intercept = intercept;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }
}
