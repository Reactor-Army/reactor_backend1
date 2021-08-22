package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import org.apache.commons.math3.fitting.leastsquares.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import java.util.List;

public class ThomasModelNumeric {

    private List<Observation> observations;

    private LeastSquaresOptimizer optimizer;

    private static final int JACOBIAN_COLUMNS = 2;
    private static final double[] SEEDS = {1.0,1.0};

    private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_EVALUATIONS = 1000;

    public ThomasModelNumeric(List<Observation> observations, LeastSquaresOptimizer optimizer) {
        this.observations = observations;
        this.optimizer = optimizer;
    }

    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     * Despues de A despejamos Kth*Co/F
     * Y de B despejamos Kth*W*Qo/F
     */
    public MultivariateJacobianFunction getModel(){
        return model -> {
            double a = model.getEntry(0);
            double b = model.getEntry(1);

            RealVector residue = new ArrayRealVector(observations.size());
            RealMatrix jacobian = new Array2DRowRealMatrix(observations.size(), JACOBIAN_COLUMNS);

            for (int i = 0; i < observations.size(); ++i) {
                Observation obs = observations.get(i);
                double modelI = simplifiedFunction(obs.getX(),a,b);
                residue.setEntry(i, modelI);

                jacobian.setEntry(i,0, simplifiedDerivativeA(obs.getX(),a,b));
                jacobian.setEntry(i,1,simplifiedDerivativeB(obs.getX(), a,b));
            }
            return new Pair<>(residue, jacobian);
        };

    }

    private double simplifiedFunction(double x, double a, double b){
        return 1 / (1 + simplifiedExponential(x,a,b));
    }

    private double simplifiedDerivativeA(double x, double a, double b){
        return (x * simplifiedExponential(x,a,b)) / Math.pow(1+ simplifiedExponential(x,a,b),2);
    }

    private double simplifiedDerivativeB(double x, double a, double b){
        return (- simplifiedExponential(x,a,b) / Math.pow(1+ simplifiedExponential(x,a,b),2));
    }

    public LeastSquaresOptimizer.Optimum getOptimum(){
        double[] yValues = new double[observations.size()];
        for (int i = 0; i < observations.size(); ++i) {
            yValues[i] = observations.get(i).getY();
        }

        LeastSquaresProblem problem = new LeastSquaresBuilder().
                start(SEEDS).
                model(this.getModel()).
                target(yValues).
                lazyEvaluation(false).
                maxEvaluations(MAX_EVALUATIONS).
                maxIterations(MAX_ITERATIONS).
                build();
        return optimizer.optimize(problem);
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


    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public LeastSquaresOptimizer getOptimizer() {
        return optimizer;
    }

    public void setOptimizer(LeastSquaresOptimizer optimizer) {
        this.optimizer = optimizer;
    }
}
