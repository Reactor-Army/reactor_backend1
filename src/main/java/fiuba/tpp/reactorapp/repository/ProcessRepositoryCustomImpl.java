package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.model.filter.ProcessFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
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

        Query query = em.createNativeQuery("SELECT COUNT(*) FROM PROCESS WHERE ADSORBATE_ID =:id");
        query.setParameter("id", adsorbateId);

        return  ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public Long getAdsorbentProcessCount(Long adsorbentId) {

        Query query = em.createNativeQuery("SELECT COUNT(*) FROM PROCESS WHERE ADSORBENT_ID =:id");
        query.setParameter("id", adsorbentId);

        return ((Number) query.getSingleResult()).longValue();

    }
}
