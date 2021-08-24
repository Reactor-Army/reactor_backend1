package fiuba.tpp.reactorapp.service.chemicalmodels;

/**
 * El modelo de Adams - Bohart establece que
 * C/C0 = exp( (Kab * Co * Vef)/ F - (Kab * No * Z)/ Uo)
 * Esto lo podemos reducir a
 * Y = exp( aX - B)
 * Donde Y es C/C0
 * y X es Vef
 * De esta manera tenemos la funcion sobre la que calculamos el jacobiano
 */
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
