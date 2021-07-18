package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.entities.Process;
import org.springframework.stereotype.Service;

@Service
public class MathService {

    /**
     * La integral de 1/x dx es ln(x) en un intervalo definido entonces la formula queda
     * -Caudal * 1/k * (Ln(cf) - ln(co))
     */
    public Double resolveFirstOrder(Process process, double initialConcentration, double finalConcentration, double flow ){
        double a = Math.log(finalConcentration) - Math.log(initialConcentration);
        double b = 1/process.getKineticConstant();
        double c = - flow;

        return a * b * c;
    }

    /**
     * La integral de 1/x^2 dx es -1/x en un intervalo definido entonces la formula queda
     * -Caudal * 1/k * (1/co - 1/cf)
     */
    public Double resolveSecondOrder(Process process, double initialConcentration, double finalConcentration, double flow ){
        double a = 1/initialConcentration - 1/finalConcentration;
        double b = 1/process.getKineticConstant();
        double c = - flow;

        return a * b * c;
    }
}
