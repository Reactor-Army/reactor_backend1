package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AdsorbateRepositoryCustomImpl implements AdsorbateRepositoryCustom {

    @Autowired
    EntityManager em;

    private static final String ADSORBATE = "adsorbate";
    private static final String ADSORBENT = "adsorbent";

    @Override
    public List<Adsorbate> getAdsorbates(AdsorbateFilter filter, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbate> cq = cb.createQuery(Adsorbate.class);
        Root<Adsorbate> adsorbate = cq.from(Adsorbate.class);

        List<Predicate> predicates = new ArrayList<>();

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(adsorbate.get("free"),true));
        }

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getName().toLowerCase());
            predicates.add(cb.or(cb.like(adsorbate.get("nameIUPACNormalized"), "%"+nombreFilter+"%"),
                                cb.like(adsorbate.get("ionNameNormalized"), "%"+nombreFilter+"%")));
        }

        if(filter.getIonCharge() != null){
            predicates.add(cb.equal(adsorbate.get("ionCharge"), filter.getIonCharge()));
        }

        if(filter.getAdsorbentId() != null){
            Subquery<Long> sub = cq.subquery(Long.class);
            Root<Process> process = sub.from(Process.class);
            sub.select(process.get(ADSORBATE).get("id")).where(cb.equal(process.get(ADSORBENT).get("id"), filter.getAdsorbentId()));
            predicates.add(cb.in(sub));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }

    @Override
    public Adsorbate getAdsorbate(Long id, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbate> cq = cb.createQuery(Adsorbate.class);
        Root<Adsorbate> adsorbate = cq.from(Adsorbate.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(adsorbate.get("id"),id));

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(adsorbate.get("free"),true));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
