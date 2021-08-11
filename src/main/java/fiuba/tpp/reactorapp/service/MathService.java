package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.exception.NotEnoughObservationsException;
import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MathService {

    private static final int NUMBER_OF_DECIMALS = 2;

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

    public RegressionResult calculateRegression(List<Observation> observations) throws NotEnoughObservationsException {
        if(observations.size() < 2) throw new NotEnoughObservationsException();

        //Crearla con true, hace que incluya le valor de b en la regresion
        SimpleRegression regression = new SimpleRegression(true);

        for (Observation observation: observations) {
            regression.addData(observation.getX(), observation.getY());
        }
        return new RegressionResult(round(regression.getIntercept()),round(regression.getSlope()));
    }

    public double ln(double value){
        return round(Math.log(value));
    }

    public ThomasResponse calculateThomas(RegressionResult regression, ThomasRequest request){
        double kth = thomasConstant(regression,request);
        double qo = thomasQo(regression,request,kth);

        return new ThomasResponse(kth,qo);
    }

    //Kth * Co / F = slope
    // Entonces
    // Kth = slope * F /Co
    private double thomasConstant(RegressionResult regression, ThomasRequest request){
        return round((regression.getSlope() * request.getCaudalVolumetrico()) / request.getConcentracionInicial());
    }

    //Una vez que tenemos Kth
    //Kth * Qo * W /F = intercept
    // Qo = intercept * F / W * Kth
    private double thomasQo(RegressionResult regression, ThomasRequest request, double thomasConstant){
        return round((regression.getIntercept() * request.getCaudalVolumetrico()) / (request.getSorbenteReactor() * thomasConstant));
    }

    private double round(double value){
        return Precision.round(value,NUMBER_OF_DECIMALS);
    }
}
