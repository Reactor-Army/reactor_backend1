package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.util.List;

public class ThomasModel {
    /**
     * Observaciones de Vef C/C0
     */
    private final List<Observation> observations;

    /**
    * Cantidad de sorbente en el reactor W
     */
    private final double w;

    /**
     * Caudal F
     */
    private final double f;

    /**
     * Concentracion inicial C0
     */
    private final double co;

    private final ThomasModelNumeric numericModel;

    private static final double  TOLERANCE = 1.0e-12;

    public ThomasModel(List<Observation> observations, double w, double f, double co) {
        this.observations = observations;
        this.w = w;
        this.f = f;
        this.co = co;
        numericModel = new ThomasModelNumeric();
    }

    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     * Despues de A despejamos Kth*Co/F
     * Y de B despejamos Kth*W*Qo/F
     */
    public ThomasResponse calculate(double[] seeds){
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer().withCostRelativeTolerance(TOLERANCE).
                withParameterRelativeTolerance(TOLERANCE);
        LeastSquaresOptimizer.Optimum optimum = numericModel.getOptimum(observations,optimizer,seeds);
        double a = optimum.getPoint().getEntry(0);
        double b = optimum.getPoint().getEntry(1);

        double kth = calculateThomasConstant(a);
        double qo = calculateThomasQo(b, kth);

        ThomasResponse response = new ThomasResponse(kth,qo);
        response.setRms(numericModel.getR2(observations,a,b));

        return response;
    }

    /**
     * a = Kth*Co/F
     * Kth = a*F /Co
     * @param a
     * @return
     */
    private double calculateThomasConstant(double a){
        return (a * f) / co;
    }

    /**
     * b = Kth*W*Qo/F
     * Qo = b * F / Kth * W
     * @param b
     * @param kth
     * @return
     */
    private double calculateThomasQo(double b, double kth){
        return (b * f) / (kth * w);
    }

    public List<Observation> getObservations() {
        return observations;
    }

}
