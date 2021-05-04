package fiuba.tpp.reactorapp.model.filter;

public class ProcessFilter {

    private Long idAdsorbate;
    private Long idAdsorbent;

    public ProcessFilter(Long idAdsorbate, Long idAdsorbente) {
        this.idAdsorbate = idAdsorbate;
        this.idAdsorbent = idAdsorbente;
    }

    public Long getIdAdsorbate() {
        return idAdsorbate;
    }

    public void setIdAdsorbate(Long idAdsorbate) {
        this.idAdsorbate = idAdsorbate;
    }

    public Long getIdAdsorbent() {
        return idAdsorbent;
    }

    public void setIdAdsorbent(Long idAdsorbent) {
        this.idAdsorbent = idAdsorbent;
    }
}
