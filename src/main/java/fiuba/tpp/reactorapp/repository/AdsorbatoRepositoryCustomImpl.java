package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbato;
import fiuba.tpp.reactorapp.model.filter.AdsorbatoFilter;
import org.apache.commons.lang3.StringUtils;
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

        if (filter.getNombre() != null && !filter.getNombre().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getNombre().toLowerCase());
            predicates.add(cb.or(cb.like(adsorbato.get("nombreIUPACNormalizado"), "%"+nombreFilter+"%"),
                                cb.like(adsorbato.get("nombreIonNormalizado"), "%"+nombreFilter+"%")));
        }

        if(filter.getCargaIon() != null){
            predicates.add(cb.equal(adsorbato.get("cargaIon"), filter.getCargaIon()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }
}
