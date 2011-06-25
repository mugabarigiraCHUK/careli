/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.controllers;

import do_.com.bpd.business.referrals.persistence.Utils;
import do_.com.bpd.business.referrals.persistence.entities.EntityChange;
import do_.com.bpd.business.referrals.persistence.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class EntityChangeJpaController implements Serializable {

    public EntityChangeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    public EntityChangeJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Utils.PERSISTENCE_UNIT_NAME);
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EntityChange entityChange) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entityChange);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EntityChange entityChange) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            entityChange = em.merge(entityChange);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entityChange.getId();
                if (findEntityChange(id) == null) {
                    throw new NonexistentEntityException("The entityChange with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntityChange entityChange;
            try {
                entityChange = em.getReference(EntityChange.class, id);
                entityChange.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entityChange with id " + id + " no longer exists.", enfe);
            }
            em.remove(entityChange);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EntityChange> findEntityChangeEntities() {
        return findEntityChangeEntities(true, -1, -1);
    }

    public List<EntityChange> findEntityChangeEntities(int maxResults, int firstResult) {
        return findEntityChangeEntities(false, maxResults, firstResult);
    }

    private List<EntityChange> findEntityChangeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EntityChange.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public EntityChange findEntityChange(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntityChange.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntityChangeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntityChange> rt = cq.from(EntityChange.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
