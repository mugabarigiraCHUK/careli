/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package do_.com.bpd.business.referrals.persistence.controllers;

import do_.com.bpd.business.referrals.persistence.Utils;
import do_.com.bpd.business.referrals.persistence.entities.Referral;
import do_.com.bpd.business.referrals.persistence.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author Administrator
 */
public class ReferralJpaController implements Serializable {

    public ReferralJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ReferralJpaController() {
        this.emf = Persistence.createEntityManagerFactory(Utils.PERSISTENCE_UNIT_NAME);
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Referral referral) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(referral);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Referral referral) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            referral = em.merge(referral);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = referral.getId();
                if (findReferral(id) == null) {
                    throw new NonexistentEntityException("The referral with id " + id + " no longer exists.");
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
            Referral referral;
            try {
                referral = em.getReference(Referral.class, id);
                referral.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The referral with id " + id + " no longer exists.", enfe);
            }
            em.remove(referral);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Referral> findReferralEntities() {
        return findReferralEntities(true, -1, -1);
    }

    public List<Referral> findReferralEntities(int maxResults, int firstResult) {
        return findReferralEntities(false, maxResults, firstResult);
    }

    private List<Referral> findReferralEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Referral.class));
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

    public Referral findReferral(Integer id) {
        EntityManager em = getEntityManager();
        try {
            Referral find = em.find(Referral.class, id);
            emf.getCache().evict(Referral.class);
            return find;
        } finally {
            em.close();
        }
    }

    public int getReferralCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Referral> rt = cq.from(Referral.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
