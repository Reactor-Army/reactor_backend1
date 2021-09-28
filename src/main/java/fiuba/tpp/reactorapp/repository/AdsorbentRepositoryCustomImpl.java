package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AdsorbentRepositoryCustomImpl implements AdsorbentRepositoryCustom {

    @Autowired
    EntityManager em;

    private static final String ADSORBATE = "adsorbate";
    private static final String ADSORBENT = "adsorbent";

    @Override
    public List<Adsorbent> getAll(AdsorbentFilter filter, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbent> cq = cb.createQuery(Adsorbent.class);

        Root<Adsorbent> adsorbent = cq.from(Adsorbent.class);
        List<Predicate> predicates = new ArrayList<>();

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(adsorbent.get("free"),true));
        }

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getName().toLowerCase());
            predicates.add(cb.like(adsorbent.get("nameNormalized"), "%"+nombreFilter+"%"));
        }

        if(filter.getAdsorbateId() != null){
            Subquery<Long> sub = cq.subquery(Long.class);
            Root<Process> process = sub.from(Process.class);
            sub.select(process.get(ADSORBENT).get("id")).where(cb.equal(process.get(ADSORBATE).get("id"), filter.getAdsorbateId()));
            predicates.add(cb.in(sub));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }

    @Override
    public Adsorbent getAdsorbent(Long id, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbent> cq = cb.createQuery(Adsorbent.class);
        Root<Adsorbent> adsorbent = cq.from(Adsorbent.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(adsorbent.get("id"),id));

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(adsorbent.get("free"),true));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
