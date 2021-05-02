package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Reactor;
import fiuba.tpp.reactorapp.model.filter.ReactorFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ReactorRepositoryCustomImpl implements  ReactorRepositoryCustom{

    @Autowired
    EntityManager em;

    @Override
    public List<Reactor> getAll(ReactorFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Reactor> cq = cb.createQuery(Reactor.class);

        Root<Reactor> reactorRoot = cq.from(Reactor.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getIdAdsorbato() != null) {
            predicates.add(cb.equal(reactorRoot.get("adsorbato").get("id"), filter.getIdAdsorbato()));
        }

        if(filter.getIdAdsorbente() != null){
            predicates.add(cb.equal(reactorRoot.get("adsorbente").get("id"),filter.getIdAdsorbente()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
