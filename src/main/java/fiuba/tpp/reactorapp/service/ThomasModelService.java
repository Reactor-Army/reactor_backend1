package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.RegressionResult;
import fiuba.tpp.reactorapp.model.request.ThomasRequest;
import fiuba.tpp.reactorapp.model.response.ThomasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThomasModelService {

    @Autowired
    private MathService mathService;

    /**
     * ln(Co/C -1) = (Kth * Qo * W)/ F - (Kth * Co)/F Vef
     * @param regression
     * @param request
     * @return
     */
    public ThomasResponse calculateThomas(RegressionResult regression, ThomasRequest request){
        double kth = mathService.round(thomasConstant(regression,request));
        double qo = mathService.round(thomasQo(regression,request,kth));

        return new ThomasResponse(kth,qo);
    }

    /**
     * -Kth * Co / F = slope
     *  Kth =- slope * F /Co
     * @param regression
     * @param request
     * @return
     */
    private double thomasConstant(RegressionResult regression, ThomasRequest request){
        return -(regression.getSlope() * request.getCaudalVolumetrico()) / request.getConcentracionInicial();
    }

    /**
     * Una vez que tenemos Kth
     * Kth * Qo * W /F = intercept
     * Qo = intercept * F / W * Kth
     * @param regression
     * @param request
     * @param thomasConstant
     * @return
     */
    private double thomasQo(RegressionResult regression, ThomasRequest request, double thomasConstant){
        return (regression.getIntercept() * request.getCaudalVolumetrico()) / (request.getSorbenteReactor() * thomasConstant);
    }

}
