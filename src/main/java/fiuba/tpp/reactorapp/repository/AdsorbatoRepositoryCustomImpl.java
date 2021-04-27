package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AdsorbatoRepositoryCustomImpl implements AdsorbatoRepositoryCustom{

    @Autowired
    EntityManager em;


    @Override
    public List<Adsorbato> getAll(AdsorbatoFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Adsorbato> cq = cb.createQuery(Adsorbato.class);

        Root<Adsorbato> adsorbato = cq.from(Adsorbato.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getNombreIUPAC() != null && !filter.getNombreIUPAC().isEmpty()) {
            predicates.add(cb.like(adsorbato.get("nombreIUPAC"), "%"+filter.getNombreIUPAC()+"%"));
        }

        if(filter.getCargaIon() != null){
            predicates.add(cb.equal(adsorbato.get("cargaIon"), filter.getCargaIon()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }
}
