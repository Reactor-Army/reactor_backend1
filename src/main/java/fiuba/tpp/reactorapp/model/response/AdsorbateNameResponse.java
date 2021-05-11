package fiuba.tpp.reactorapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiuba.tpp.reactorapp.entities.Adsorbate;
import org.apache.commons.lang3.StringUtils;

public class AdsorbateNameResponse {

    private Long id;

    @JsonProperty("nombre")
    private String name;

    public AdsorbateNameResponse(Adsorbate adsorbate) {
        this.id = adsorbate.getId();
        this.name = formatName(adsorbate);
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

    private String formatName(Adsorbate adsorbate){
        if(adsorbate.getNameIUPAC() != null){
            return StringUtils.capitalize(adsorbate.getIonName()) + " ("+ adsorbate.getNameIUPAC() +")";
        }
        return StringUtils.capitalize(adsorbate.getIonName());
    }


}
