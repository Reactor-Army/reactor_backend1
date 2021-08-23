package fiuba.tpp.reactorapp.service.chemicalmodels;

public class YoonNelsonModelNumeric implements NumericModel{

    @Override
    public double simplifiedDerivativeB(double x, double a, double b) {
        return - simplifiedExponential(x,a,b) / Math.pow(1+ simplifiedExponential(x,a,b),2) ;
    }

    @Override
    public double simplifiedDerivativeA(double x, double a, double b) {
        return x * simplifiedExponential(x,a,b) / Math.pow(1+ simplifiedExponential(x,a,b),2);
    }

    @Override
    public double simplifiedFunction(double x, double a, double b) {
        return simplifiedExponential(x,a,b) / 1 + simplifiedExponential(x,a,b);
    }

    private double simplifiedExponential(double x, double a, double b){
        return Math.exp(a*x - b);
    }
}
