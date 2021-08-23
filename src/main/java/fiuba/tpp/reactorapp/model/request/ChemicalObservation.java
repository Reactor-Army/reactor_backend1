package fiuba.tpp.reactorapp.model.request;

public class ChemicalObservation {

    private double volumenEfluente;

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
