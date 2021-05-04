package fiuba.tpp.reactorapp.model.filter;

public class AdsorbateFilter {

    private String name;
    private Integer ionCharge;

    public AdsorbateFilter(String name, Integer ionCharge) {
        this.name = name;
        this.ionCharge = ionCharge;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIonCharge() {
        return ionCharge;
    }

    public void setIonCharge(Integer ionCharge) {
        this.ionCharge = ionCharge;
    }
}
