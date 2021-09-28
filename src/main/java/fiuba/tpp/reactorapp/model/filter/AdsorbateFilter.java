package fiuba.tpp.reactorapp.model.filter;

public class AdsorbateFilter {

    private String name;
    private Integer ionCharge;
    private Long adsorbentId;

    public AdsorbateFilter() {
    }

    public AdsorbateFilter(String name, Integer ionCharge) {
        this.name = name;
        this.ionCharge = ionCharge;
    }

    public AdsorbateFilter(String name) {
        this.name = name;
    }

    public AdsorbateFilter(String name, Long adsorbentId) {
        this.name = name;
        this.adsorbentId = adsorbentId;
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

    public Long getAdsorbentId() {
        return adsorbentId;
    }

    public void setAdsorbentId(Long adsorbentId) {
        this.adsorbentId = adsorbentId;
    }
}
