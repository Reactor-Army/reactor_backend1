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

public class ProcessRepositoryCustomImpl implements ProcessRepositoryCustom {

    @Autowired
    EntityManager em;

    private static final String ADSORBATE = "adsorbate";
    private static final String ADSORBENT = "adsorbent";

    @Override
    public List<Process> getAll(ProcessFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);

        Root<Process> processRoot = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getIdAdsorbate() != null) {
            predicates.add(cb.equal(processRoot.get(ADSORBATE).get("id"), filter.getIdAdsorbate()));
        }

        if(filter.getIdAdsorbent() != null){
            predicates.add(cb.equal(processRoot.get(ADSORBENT).get("id"),filter.getIdAdsorbent()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(processRoot.get("qmax")));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Process> getByAdsorbates(List<Long> adsorbatesIds) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);

        Root<Process> processRoot = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if(adsorbatesIds != null && !adsorbatesIds.isEmpty()){
            predicates.add(processRoot.get(ADSORBATE).get("id").in(adsorbatesIds));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(processRoot.get("qmax")));

        return em.createQuery(cq).getResultList();

    }

    @Override
    public Long getAdsorbateProcessCount(Long adsorbateId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        cq.select(cb.count(cq.from(Process.class)));

        Root<Process> processRoot = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if(adsorbateId != null){
            predicates.add(cb.equal(processRoot.get(ADSORBATE).get("id"), adsorbateId));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public Long getAdsorbentProcessCount(Long adsorbentId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        cq.select(cb.count(cq.from(Process.class)));

        Root<Process> processRoot = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if(adsorbentId != null){
            predicates.add(cb.equal(processRoot.get(ADSORBENT).get("id"), adsorbentId));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
