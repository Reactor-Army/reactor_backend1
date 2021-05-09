package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbent;
import fiuba.tpp.reactorapp.model.filter.AdsorbentFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AdsorbentRepositoryCustomImpl implements AdsorbentRepositoryCustom {

    @Autowired
    EntityManager em;


    @Override
    public List<Adsorbent> getAll(AdsorbentFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbent> cq = cb.createQuery(Adsorbent.class);

        Root<Adsorbent> adsorbent = cq.from(Adsorbent.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getName().toLowerCase());
            predicates.add(cb.like(adsorbent.get("nameNormalized"), "%"+nombreFilter+"%"));
        }

       

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }
}
