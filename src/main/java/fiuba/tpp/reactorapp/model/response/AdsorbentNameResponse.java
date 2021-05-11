package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Adsorbent;
import org.apache.commons.lang3.StringUtils;

public class AdsorbentNameResponse {

    private Long id;

    @JsonProperty("nombre")
    private String name;

    public AdsorbentNameResponse() {
    }

    public AdsorbentNameResponse(Adsorbent adsorbent){
        this.id = adsorbent.getId();
        this.name = formatName(adsorbent);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String formatName(Adsorbent adsorbent){
        String particleSize = (adsorbent.getParticleSize()!=null) ? adsorbent.getParticleSize(): "-";
        return StringUtils.capitalize(adsorbent.getName()) + " (" + particleSize +")";
    }
}
