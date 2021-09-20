package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MathService {

    private static final int NUMBER_OF_DECIMALS = 3;

    /**
     * La integral de 1/x dx es ln(x) en un intervalo definido entonces la formula queda
     * -Caudal * 1/k * (Ln(cf) - ln(co))
     */
    public Double resolveFirstOrder(Process process, double initialConcentration, double finalConcentration, double flow ){
        double a = Math.log(finalConcentration) - Math.log(initialConcentration);
        double b = 1/process.getKineticConstant();
        double c = - flow;

        return round(a * b * c);
    }

    /**
     * La integral de 1/x^2 dx es -1/x en un intervalo definido entonces la formula queda
     * -Caudal * 1/k * (1/co - 1/cf)
     */
    public Double resolveSecondOrder(Process process, double initialConcentration, double finalConcentration, double flow ){
        double a = 1/initialConcentration - 1/finalConcentration;
        double b = 1/process.getKineticConstant();
        double c = - flow;

        return round(a * b * c);
    }

    public RegressionResult calculateRegression(List<Observation> observations){
        if(observations.size() < 2){
            return new RegressionResult(1D,1D);
        }

        //Crearla con true, hace que incluya le valor de b en la regresion
        SimpleRegression regression = new SimpleRegression(true);

        for (Observation observation: observations) {
            regression.addData(observation.getX(), observation.getY());
        }
        RegressionResult result = new RegressionResult();
        result.setSlope(Double.isNaN(round(regression.getSlope()))? 1D: round(regression.getSlope()));
        result.setIntercept(Double.isNaN(round(regression.getIntercept()))? 1D : round(regression.getIntercept()));
        return result;
    }

    public double ln(double value){
        return Math.log(value);
    }

    public double round(double value){
        return Precision.round(value,NUMBER_OF_DECIMALS);
    }
}
