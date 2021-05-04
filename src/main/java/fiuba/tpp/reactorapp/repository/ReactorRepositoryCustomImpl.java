package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
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
    public List<Process> getAll(ProcessFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);

        Root<Process> processRoot = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getIdAdsorbate() != null) {
            predicates.add(cb.equal(processRoot.get("adsorbate").get("id"), filter.getIdAdsorbate()));
        }

        if(filter.getIdAdsorbent() != null){
            predicates.add(cb.equal(processRoot.get("adsorbent").get("id"),filter.getIdAdsorbent()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
