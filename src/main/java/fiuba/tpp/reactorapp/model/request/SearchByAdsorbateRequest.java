package fiuba.tpp.reactorapp.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchByAdsorbateRequest {

    @JsonProperty("adsorbatos")
    private List<Long> adsorbatesIds;

    public SearchByAdsorbateRequest() {
    }

    public SearchByAdsorbateRequest(List<Long> adsorbatesIds) {
        this.adsorbatesIds = adsorbatesIds;
    }

    public List<Long> getAdsorbatesIds() {
        return adsorbatesIds;
    }

    public void setAdsorbatesIds(List<Long> adsorbatesIds) {
        this.adsorbatesIds = adsorbatesIds;
    }
}
