/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.dao.jpacontroller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.SourceTypeLkp;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class SourceTypeLkpJpaController implements Serializable {

    public SourceTypeLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SourceTypeLkp sourceTypeLkp) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sourceTypeLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SourceTypeLkp sourceTypeLkp) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sourceTypeLkp = em.merge(sourceTypeLkp);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = sourceTypeLkp.getId();
                if (findSourceTypeLkp(id) == null) {
                    throw new NonexistentEntityException("The sourceTypeLkp with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SourceTypeLkp sourceTypeLkp;
            try {
                sourceTypeLkp = em.getReference(SourceTypeLkp.class, id);
                sourceTypeLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sourceTypeLkp with id " + id + " no longer exists.", enfe);
            }
            em.remove(sourceTypeLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SourceTypeLkp> findSourceTypeLkpEntities() {
        return findSourceTypeLkpEntities(true, -1, -1);
    }

    public List<SourceTypeLkp> findSourceTypeLkpEntities(int maxResults, int firstResult) {
        return findSourceTypeLkpEntities(false, maxResults, firstResult);
    }

    private List<SourceTypeLkp> findSourceTypeLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SourceTypeLkp.class));
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

    public SourceTypeLkp findSourceTypeLkp(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SourceTypeLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getSourceTypeLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SourceTypeLkp> rt = cq.from(SourceTypeLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
