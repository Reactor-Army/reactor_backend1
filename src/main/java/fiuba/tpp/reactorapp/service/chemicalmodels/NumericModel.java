package fiuba.tpp.reactorapp.service.chemicalmodels;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.service.utils.LeastSquareResolver;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import java.util.List;

public interface NumericModel {

    int JACOBIAN_COLUMNS = 2;

    LeastSquareResolver leastSquareResolver = new LeastSquareResolver();


    /**
     * Obtiene el modelo para el optimizador elegido
     * En este caso el vector va a tener en la posicion 0 a A y en la 1 a B
     */
    default MultivariateJacobianFunction getModel(List<Observation> observations){
        return model -> {
            double a = model.getEntry(0);
            double b = model.getEntry(1);

            RealVector values = new ArrayRealVector(observations.size());
            RealMatrix jacobian = new Array2DRowRealMatrix(observations.size(), JACOBIAN_COLUMNS);

            for (int i = 0; i < observations.size(); ++i) {
                Observation obs = observations.get(i);
                double modelI = simplifiedFunction(obs.getX(),a,b);
                values.setEntry(i, modelI);

                jacobian.setEntry(i,0, simplifiedDerivativeA(obs.getX(),a,b));
                jacobian.setEntry(i,1,simplifiedDerivativeB(obs.getX(), a,b));
            }
            return new Pair<>(values, jacobian);
        };
    }

    default LeastSquaresOptimizer.Optimum getOptimum(List<Observation> observations,  LeastSquaresOptimizer optimizer, double[] seeds){
        return leastSquareResolver.getOptimum(observations,getModel(observations),optimizer, seeds);
    }

    double simplifiedDerivativeB(double x, double a, double b);

    double simplifiedDerivativeA(double x, double a, double b);

    double simplifiedFunction(double x, double a, double b);

}
