package fiuba.tpp.reactorapp.model.filter;

public class AdsorbentFilter {

    private String name;

    private Long adsorbateId;

    public AdsorbentFilter(){}

    public AdsorbentFilter(String name) {
        this.name = name;
    }

    public AdsorbentFilter(String name, Long adsorbateId) {
        this.name = name;
        this.adsorbateId = adsorbateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAdsorbateId() {
        return adsorbateId;
    }

    public void setAdsorbateId(Long adsorbateId) {
        this.adsorbateId = adsorbateId;
    }
}
