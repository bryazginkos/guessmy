package dao;

import model.entity.User;
import play.db.jpa.JPA;
import services.exceptions.SystemException;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by Константин on 12.09.2015.
 */
@Singleton
public class UserDAO  {

    public User findByEmail(String email) throws SystemException {
        if (email == null) return null;
        EntityManager em = JPA.em();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> from  = cq.from(User.class);

        cq.where(cb.equal(from.get("email"), email));
        TypedQuery<User> result = em.createQuery(cq);
        try {
            return result.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable e) {
            throw new SystemException(e);
        }
    }

    public void save(User user) throws SystemException {
        try {
            JPA.em().persist(user);
        } catch (Throwable e) {
            throw new SystemException(e);
        }

    }
}
