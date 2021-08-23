package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.YoonNelsonResponse;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.List;

public class YoonNelsonModel {

    /**
     * Observaciones de Vef C/C0
     */
    private final List<Observation> observations;

    /**
     * Caudal F
     */
    private final double f;


    private static final double  TOLERANCE = 1.0e-12;

    private final YoonNelsonModelNumeric numericModel;

    public YoonNelsonModel(List<Observation> observations, double f) {
        this.observations = observations;
        this.f = f;
        numericModel = new YoonNelsonModelNumeric();
    }

    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     * Despues de A despejamos Kyn/F
     * Y de B despejamos Kyn * t50
     */
    public YoonNelsonResponse calculate(double[] seeds){
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer().withCostRelativeTolerance(TOLERANCE).
                withParameterRelativeTolerance(TOLERANCE);
        LeastSquaresOptimizer.Optimum optimum = numericModel.getOptimum(observations,optimizer, seeds);

        double a = optimum.getPoint().getEntry(0);
        double b = optimum.getPoint().getEntry(1);

        double kyn = calculateYoonNelsonConstant(a);
        double t50 = calculateTimeFiftyPercent(b, kyn);

        return new YoonNelsonResponse(kyn,t50);
    }

    /**
     * a = Kyn/F
     * Kyn = a*F
     * @param a
     * @return
     */
    private double calculateYoonNelsonConstant(double a){
        return a * f;
    }
    /**
     * b = Kyn*t50
     * t50 = b/ kyn
     * @param b
     * @param kyn
     * @return
     */
    private double calculateTimeFiftyPercent(double b, double kyn){
        return b / kyn;
    }
}
