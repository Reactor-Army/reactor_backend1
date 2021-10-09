package fiuba.tpp.reactorapp.repository.auth;

import fiuba.tpp.reactorapp.entities.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    @Autowired
    EntityManager em;


    @Override
    public List<User> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        Date minDate = new Date(0L);

        cq.orderBy(cb.desc(user.get("role")),cb.desc(cb.coalesce(user.get("lastLogin"), minDate)));

        return em.createQuery(cq).getResultList();
    }
}
