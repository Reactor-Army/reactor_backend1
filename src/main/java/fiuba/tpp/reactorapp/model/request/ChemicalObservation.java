package fiuba.tpp.reactorapp.model.request;

import com.opencsv.bean.CsvBindByName;

public class ChemicalObservation {

    @CsvBindByName(column = "volumenEfluente", required = true)
    private double volumenEfluente;

    @CsvBindByName(column = "concentracionSalida", required = true)
    private double concentracionSalida;

    public ChemicalObservation() {
    }

    public ChemicalObservation(double volumenEfluente, double concentracionSalida) {
        this.volumenEfluente = volumenEfluente;
        this.concentracionSalida = concentracionSalida;
    }

    public double getVolumenEfluente() {
        return volumenEfluente;
    }

    public void setVolumenEfluente(double volumenEfluente) {
        this.volumenEfluente = volumenEfluente;
    }

    public double getConcentracionSalida() {
        return concentracionSalida;
    }

    public void setConcentracionSalida(double concentracionSalida) {
        this.concentracionSalida = concentracionSalida;
    }
}
