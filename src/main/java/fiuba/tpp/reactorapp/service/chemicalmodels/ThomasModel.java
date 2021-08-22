package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.util.List;

public class ThomasModel {


    private List<Observation> observations;

    private double w;

    private double f;

    private double co;

    private ThomasModelNumeric numericModel;

    public ThomasModel(List<Observation> observations, double w, double f, double co) {
        this.observations = observations;
        this.w = w;
        this.f = f;
        this.co = co;
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer().withCostRelativeTolerance(1.0e-12).
                withParameterRelativeTolerance(1.0e-12);
        numericModel = new ThomasModelNumeric(observations, optimizer);
    }

    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     * Despues de A despejamos Kth*Co/F
     * Y de B despejamos Kth*W*Qo/F
     */
    public ThomasResponse calculate(){
        LeastSquaresOptimizer.Optimum optimum = numericModel.getOptimum();
        double a = optimum.getPoint().getEntry(0);
        double b = optimum.getPoint().getEntry(1);

        double kth = thomasConstant(a);
        double qo = thomasQo(b, kth);

        return new ThomasResponse(kth,qo);
    }

    /**
     * a = Kth*Co/F
     * Kth = a*F /Co
     * @param a
     * @return
     */
    private double thomasConstant(double a){
        return (a * f) / co;
    }

    /**
     * b = Kth*W*Qo/F
     * Qo = b * F / Kth * W
     * @param b
     * @param kth
     * @return
     */
    private double thomasQo(double b, double kth){
        return (b * f) / (kth * w);
    }

    public List<Observation> getObservations() {
        return observations;
    }

}
