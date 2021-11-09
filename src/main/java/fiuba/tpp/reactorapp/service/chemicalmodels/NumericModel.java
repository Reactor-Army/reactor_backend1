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

    /**
     * Esto siempre debe ser par
     */
    double N_INTEGRATION = 1000;

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

    default double getR2(List<Observation> observations, double a, double b){
        double e2 = 0D;
        double yMean = getMeanY(observations);
        double denominator = 0D;
        for (Observation ob: observations) {
            double e = (simplifiedFunction(ob.getX(),a,b) - ob.getY());
            e2 +=  (e*e);
            double meanError = (ob.getY() - yMean);
            denominator += (meanError * meanError) ;
        }
        if(denominator > 0){
            return 1 - (e2/denominator);
        }
        return 0D;
    }

    default double getMeanY(List<Observation> observations){
        double yMean = 0D;
        for (Observation ob:observations) {
            yMean += ob.getY();
        }
        return (yMean/ observations.size());
    }

    /**
    Asumimos que la integracion siempre tiene como limite inferior 0
     * Se integra con el metodo de simpson
     * donde dx/3 * (f(x0) + 4f(x1) + 2f(x2) + ... + f(xn)
     * dx = (b -a) / n
     *
     */
    default double numericIntegration(double limit, double a, double b){
        //Tamanio de paso
        double h = (limit) / (N_INTEGRATION);

        // Primer y ultimo termino
        double sum = (1.0 / 3.0) * (simplifiedFunction(0,a,b)+ simplifiedFunction(limit,a,b));

        // 4/3 terms
        for (int i = 1; i < N_INTEGRATION - 1; i += 2) {
            double x = h * i;
            sum += (4.0 / 3.0) * simplifiedFunction(x,a,b);
        }

        // 2/3 terms
        for (int i = 2; i < N_INTEGRATION - 1; i += 2) {
            double x = h * i;
            sum += (2.0 / 3.0) * simplifiedFunction(x,a,b);
        }

        return sum * h;
    }

    double simplifiedDerivativeB(double x, double a, double b);

    double simplifiedDerivativeA(double x, double a, double b);

    double simplifiedFunction(double x, double a, double b);

}
