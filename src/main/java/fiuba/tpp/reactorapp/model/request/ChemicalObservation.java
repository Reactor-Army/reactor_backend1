package fiuba.tpp.reactorapp.model.request;

import com.opencsv.bean.CsvBindByName;

public class ChemicalObservation {

    @CsvBindByName(column = "tiempo", required = true)
    private double tiempo;

    @CsvBindByName(column = "concentracionSalida", required = true)
    private double concentracionSalida;

    public ChemicalObservation() {
    }

    public ChemicalObservation(double tiempo, double concentracionSalida) {
        this.tiempo = tiempo;
        this.concentracionSalida = concentracionSalida;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getConcentracionSalida() {
        return concentracionSalida;
    }

    public void setConcentracionSalida(double concentracionSalida) {
        this.concentracionSalida = concentracionSalida;
    }
}
