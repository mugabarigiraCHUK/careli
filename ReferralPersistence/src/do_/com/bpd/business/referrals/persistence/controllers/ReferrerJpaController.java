/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.controllers;

import do_.com.bpd.business.referrals.persistence.Utils;
import do_.com.bpd.business.referrals.persistence.controllers.exceptions.NonexistentEntityException;
import do_.com.bpd.business.referrals.persistence.entities.Referrer;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Administrator
 */
public class ReferrerJpaController implements Serializable {

    public ReferrerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ReferrerJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Utils.PERSISTENCE_UNIT_NAME);
    }
    
    
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referrer referrer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(referrer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referrer referrer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            referrer = em.merge(referrer);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = referrer.getId();
                if (findReferrer(id) == null) {
                    throw new NonexistentEntityException("The referrer with id " + id + " no longer exists.");
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
            Referrer referrer;
            try {
                referrer = em.getReference(Referrer.class, id);
                referrer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referrer with id " + id + " no longer exists.", enfe);
            }
            em.remove(referrer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Referrer> findReferrerEntities() {
        return findReferrerEntities(true, -1, -1);
    }

    public List<Referrer> findReferrerEntities(int maxResults, int firstResult) {
        return findReferrerEntities(false, maxResults, firstResult);
    }

    private List<Referrer> findReferrerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referrer.class));
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

    public Referrer findReferrer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Referrer.class, id);
        } finally {
            em.close();
        }
    }

    public int getReferrerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referrer> rt = cq.from(Referrer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
