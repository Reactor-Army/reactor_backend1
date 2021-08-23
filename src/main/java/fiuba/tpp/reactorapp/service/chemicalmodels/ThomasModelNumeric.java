package fiuba.tpp.reactorapp.service.chemicalmodels;

public class ThomasModelNumeric implements NumericModel {

    @Override
    public double simplifiedFunction(double x, double a, double b){
        return 1 / (1 + simplifiedExponential(x,a,b));
    }

    @Override
    public double simplifiedDerivativeA(double x, double a, double b){
        return (x * simplifiedExponential(x,a,b)) / Math.pow(1+ simplifiedExponential(x,a,b),2);
    }

    @Override
    public double simplifiedDerivativeB(double x, double a, double b){
        return (- simplifiedExponential(x,a,b) / Math.pow(1+ simplifiedExponential(x,a,b),2));
    }

    /**
     * exp(b-ax)
     * @param x
     * @param a
     * @param b
     * @return
     */
    private double simplifiedExponential(double x, double a, double b){
        return Math.exp(b-a*x);
    }

}
