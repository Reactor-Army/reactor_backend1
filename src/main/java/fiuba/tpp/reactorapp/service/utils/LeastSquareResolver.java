package fiuba.tpp.reactorapp.service.utils;

import fiuba.tpp.reactorapp.model.math.Observation;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;

import java.util.List;

public class LeastSquareResolver {

    private static final int MAX_ITERATIONS = 1000;
    private static final int MAX_EVALUATIONS = 1000;


    public LeastSquaresOptimizer.Optimum getOptimum(List<Observation> observations, MultivariateJacobianFunction model, LeastSquaresOptimizer optimizer, double[] seeds){
        double[] yValues = new double[observations.size()];
        for (int i = 0; i < observations.size(); ++i) {
            yValues[i] = observations.get(i).getY();
        }

        LeastSquaresProblem problem = new LeastSquaresBuilder().
                start(seeds).
                model(model).
                target(yValues).
                lazyEvaluation(false).
                maxEvaluations(MAX_EVALUATIONS).
                maxIterations(MAX_ITERATIONS).
                build();
        return optimizer.optimize(problem);
    }
}
