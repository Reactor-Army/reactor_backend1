package fiuba.tpp.reactorapp.service.chemicalmodels;

public class AdamsBohartModelNumeric implements NumericModel{

    @Override
    public double simplifiedDerivativeB(double x, double a, double b) {
        return - simplifiedExponential(x,a,b);
    }

    @Override
    public double simplifiedDerivativeA(double x, double a, double b) {
        return x * simplifiedExponential(x,a,b);
    }

    @Override
    public double simplifiedFunction(double x, double a, double b) {
        return simplifiedExponential(x,a,b);
    }

    private double simplifiedExponential(double x, double a, double b){
        return Math.exp(a*x - b);
    }
}
