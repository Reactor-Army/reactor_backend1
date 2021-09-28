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
    public List<Process> getAll(ProcessFilter filter, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);

        Root<Process> process = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(process.get("free"),true));
        }

        if (filter.getIdAdsorbate() != null) {
            predicates.add(cb.equal(process.get(ADSORBATE).get("id"), filter.getIdAdsorbate()));
        }

        if(filter.getIdAdsorbent() != null){
            predicates.add(cb.equal(process.get(ADSORBENT).get("id"),filter.getIdAdsorbent()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(process.get("qmax")));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Process> getByAdsorbates(List<Long> adsorbatesIds, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);

        Root<Process> process = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(process.get("free"),true));
        }

        if(adsorbatesIds != null && !adsorbatesIds.isEmpty()){
            predicates.add(process.get(ADSORBATE).get("id").in(adsorbatesIds));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(process.get("qmax")));

        return em.createQuery(cq).getResultList();

    }

    @Override
    public Process getProcess(Long id, Boolean isAnonymous) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Process> cq = cb.createQuery(Process.class);
        Root<Process> process = cq.from(Process.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(process.get("id"),id));

        if(Boolean.TRUE.equals(isAnonymous)){
            predicates.add(cb.equal(process.get("free"),true));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getSingleResult();

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
