package fiuba.tpp.reactorapp.model.math;

import com.opencsv.bean.CsvBindByName;

public class Observation {

    @CsvBindByName
    private double x;

    @CsvBindByName
    private double y;

    public Observation() {
    }

    public Observation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
