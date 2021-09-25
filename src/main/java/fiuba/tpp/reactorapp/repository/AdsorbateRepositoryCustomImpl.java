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


    @Override
    public List<Adsorbate> getAll(AdsorbateFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbate> cq = cb.createQuery(Adsorbate.class);
        Root<Adsorbate> adsorbate = cq.from(Adsorbate.class);


        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getName().toLowerCase());
            predicates.add(cb.or(cb.like(adsorbate.get("nameIUPACNormalized"), "%"+nombreFilter+"%"),
                                cb.like(adsorbate.get("ionNameNormalized"), "%"+nombreFilter+"%")));
        }

        if(filter.getIonCharge() != null){
            predicates.add(cb.equal(adsorbate.get("ionCharge"), filter.getIonCharge()));
        }

        if(filter.getAdsorbentId() != null){
            Subquery sub = cq.subquery(Long.class);
            Root process = sub.from(Process.class);
            sub.select(process.get("adsorbate").get("id")).where(cb.equal(process.get("adsorbent").get("id"), filter.getAdsorbentId()));
            predicates.add(cb.in(sub));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }
}
