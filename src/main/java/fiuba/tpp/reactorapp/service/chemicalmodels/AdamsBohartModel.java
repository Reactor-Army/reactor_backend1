package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.AdamsBohartResponse;
import fiuba.tpp.reactorapp.model.response.chemicalmodels.ThomasResponse;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.List;

public class AdamsBohartModel {

    /**
     * Observaciones de Vef C/C0
     */
    private final List<Observation> observations;

    /**
     * Velocidad lineal del liquido U0
     */
    private final double uo;

    /**
     * Altura del lecho del reactor Z
     */
    private final double z;

    /**
     * Caudal F
     */
    private final double f;

    /**
     * Concentracion inicial C0
     */
    private final double co;

    private final AdamsBohartModelNumeric numericModel;

    private static final double  TOLERANCE = 1.0e-12;

    public AdamsBohartModel(List<Observation> observations, double uo, double z ,double f, double co) {
        this.observations = observations;
        this.uo = uo;
        this.z = z;
        this.f = f;
        this.co = co;
        numericModel = new AdamsBohartModelNumeric();
    }

    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     * Despues de A despejamos Kab*Co/F
     * Y de B despejamos Kab*No*Z/Uo
     */
    public AdamsBohartResponse calculate(double[] seeds){
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer().withCostRelativeTolerance(TOLERANCE).
                withParameterRelativeTolerance(TOLERANCE);
        LeastSquaresOptimizer.Optimum optimum = numericModel.getOptimum(observations,optimizer,seeds);
        double a = optimum.getPoint().getEntry(0);
        double b = optimum.getPoint().getEntry(1);

        double kab = calculateAdamsBohartConstant(a);
        double no = calculateAdamsBohartNo(b, kab);

        return new AdamsBohartResponse(kab,no);
    }

    /**
     * a = Kab * Co/F
     * Kab = a*F / Co
     * @param a
     * @return
     */
    private double calculateAdamsBohartConstant(double a) {
        return (a * f) / co;

    }

    /**
     * b = Kab * No * Z / Uo
     * @param b
     * @param kab
     * @return
     */
    private double calculateAdamsBohartNo(double b, double kab) {
        return (b * uo) / (kab * z);
    }



}
