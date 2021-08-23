package fiuba.tpp.reactorapp.model.request;

import com.opencsv.bean.CsvBindByName;

public class ChemicalObservation {

    @CsvBindByName(column = "volumenEfluente", required = true)
    private double volumenEfluente;

    @CsvBindByName(column = "C/C0", required = true)
    private double relacionConcentraciones;

    public double getVolumenEfluente() {
        return volumenEfluente;
    }

    public void setVolumenEfluente(double volumenEfluente) {
        this.volumenEfluente = volumenEfluente;
    }

    public double getRelacionConcentraciones() {
        return relacionConcentraciones;
    }

    public void setRelacionConcentraciones(double relacionConcentraciones) {
        this.relacionConcentraciones = relacionConcentraciones;
    }
}
