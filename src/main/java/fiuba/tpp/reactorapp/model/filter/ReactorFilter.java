package fiuba.tpp.reactorapp.model.filter;

public class ReactorFilter {

    private Long idAdsorbato;
    private Long idAdsorbente;

    public ReactorFilter(Long idAdsorbato, Long idAdsorbente) {
        this.idAdsorbato = idAdsorbato;
        this.idAdsorbente = idAdsorbente;
    }

    public Long getIdAdsorbato() {
        return idAdsorbato;
    }

    public void setIdAdsorbato(Long idAdsorbato) {
        this.idAdsorbato = idAdsorbato;
    }

    public Long getIdAdsorbente() {
        return idAdsorbente;
    }

    public void setIdAdsorbente(Long idAdsorbente) {
        this.idAdsorbente = idAdsorbente;
    }
}
