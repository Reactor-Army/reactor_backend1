package fiuba.tpp.reactorapp.service;

import fiuba.tpp.reactorapp.model.math.Observation;
import fiuba.tpp.reactorapp.model.math.RegressionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeedService {

    @Autowired
    private MathService mathService;

    public double[] generateSeedForThomas(List<Observation> observations){
        List<Observation> points = generatePointsLinearRegressionThomas(observations);
        RegressionResult result = mathService.calculateRegression(points);
        double[] seeds = new double[2];
        seeds[0] = - result.getSlope();
        seeds[1] = result.getIntercept();
        return  seeds;
    }

    public double[] generateSeedForYoonNelson(List<Observation> observations){
        List<Observation> points = generatePointsLinearRegressionYoonNelson(observations);
        RegressionResult result = mathService.calculateRegression(points);
        double[] seeds = new double[2];
        seeds[0] = result.getSlope();
        seeds[1] = - result.getIntercept();
        return  seeds;
    }

    public double[] generateSeedForAdamsBohart(List<Observation> observations){
        List<Observation> points = generatePointsLinearRegressionAdamsBohart(observations);
        RegressionResult result = mathService.calculateRegression(points);
        double[] seeds = new double[2];
        seeds[0] = result.getSlope();
        seeds[1] = - result.getIntercept();
        return  seeds;
    }

    private List<Observation> generatePointsLinearRegressionThomas(List<Observation> observations){
        List<Observation> points = new ArrayList<>();
        for (Observation obs: observations) {
            Observation point = new Observation();
            point.setX(obs.getX());
            point.setY(transformLinearThomas(obs.getY()));
            if(!Double.isNaN(point.getY())){
                points.add(point);
            }
        }
        return points;
    }

    private List<Observation> generatePointsLinearRegressionYoonNelson(List<Observation> observations){
        List<Observation> points = new ArrayList<>();
        for (Observation obs: observations) {
            Observation point = new Observation();
            point.setX(obs.getX());
            point.setY(transformLinearYoonNelson(obs.getY()));
            if(!Double.isNaN(point.getY())){
                points.add(point);
            }
        }
        return points;
    }

    private List<Observation> generatePointsLinearRegressionAdamsBohart(List<Observation> observations){
        List<Observation> points = new ArrayList<>();
        for (Observation obs: observations) {
            Observation point = new Observation();
            point.setX(obs.getX());
            point.setY(transformLinearAdamsBohart(obs.getY()));
            if(!Double.isNaN(point.getY())){
                points.add(point);
            }
        }
        return points;
    }

    private double transformLinearThomas(double value){
        return mathService.ln((1/value) -1);
    }

    private double transformLinearYoonNelson(double value){
        return mathService.ln(value);
    }

    private double transformLinearAdamsBohart(double value){
        return mathService.ln(1/value);
    }
}
