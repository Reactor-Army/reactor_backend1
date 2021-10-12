package fiuba.tpp.reactorapp.repository;

import fiuba.tpp.reactorapp.entities.Adsorbate;
import fiuba.tpp.reactorapp.entities.Process;
import fiuba.tpp.reactorapp.entities.TesisFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TesisFileRepositoryCustomImpl implements TesisFileRepositoryCustom {


    @Autowired
    EntityManager em;

    @Override
    public List<TesisFile> getAll(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TesisFile> cq = cb.createQuery(TesisFile.class);
        Root<TesisFile> tesisFile = cq.from(TesisFile.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            String tesisFilter = StringUtils.stripAccents(name.toLowerCase());
            predicates.add(cb.or(cb.like(tesisFile.get("nameNormalized"), "%"+tesisFilter+"%"),
                    cb.like(tesisFile.get("authorNormalized"), "%"+tesisFilter+"%")));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        Date minDate = new Date(0L);
        cq.orderBy(cb.desc(cb.coalesce(tesisFile.get("publicationDate"), minDate)));

        return em.createQuery(cq).getResultList();


    }
}
