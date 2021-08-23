package fiuba.tpp.reactorapp.model.request;

import com.opencsv.bean.CsvBindByName;

public class ChemicalObservationCSV {

    @CsvBindByName(column = "volumenEfluente", required = true)
    private String volumenEfluente;

    @CsvBindByName(column = "C/C0", required = true)
    private String relacionConcentraciones;

    public String getVolumenEfluente() {
        return volumenEfluente;
    }

    public void setVolumenEfluente(String volumenEfluente) {
        this.volumenEfluente = volumenEfluente;
    }

    public String getRelacionConcentraciones() {
        return relacionConcentraciones;
    }

    public void setRelacionConcentraciones(String relacionConcentraciones) {
        this.relacionConcentraciones = relacionConcentraciones;
    }
}
