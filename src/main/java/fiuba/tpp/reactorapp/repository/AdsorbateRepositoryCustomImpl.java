package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.model.filter.AdsorbateFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

        if (filter.getNombre() != null && !filter.getNombre().isEmpty()) {
            String nombreFilter = StringUtils.stripAccents(filter.getNombre().toLowerCase());
            predicates.add(cb.or(cb.like(adsorbate.get("nombreIUPACNormalizado"), "%"+nombreFilter+"%"),
                                cb.like(adsorbate.get("nombreIonNormalizado"), "%"+nombreFilter+"%")));
        }

        if(filter.getCargaIon() != null){
            predicates.add(cb.equal(adsorbate.get("cargaIon"), filter.getCargaIon()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();


    }
}
