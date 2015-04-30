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
import org.scify.democracit.dao.model.Consultation;
import org.scify.democracit.dao.model.RelevantMat;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class RelevantMatJpaController implements Serializable {

    public RelevantMatJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RelevantMat relevantMat) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultation consultationId = relevantMat.getConsultationId();
            if (consultationId != null) {
                consultationId = em.getReference(consultationId.getClass(), consultationId.getId());
                relevantMat.setConsultationId(consultationId);
            }
            em.persist(relevantMat);
            if (consultationId != null) {
                consultationId.getRelevantMatCollection().add(relevantMat);
                consultationId = em.merge(consultationId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RelevantMat relevantMat) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RelevantMat persistentRelevantMat = em.find(RelevantMat.class, relevantMat.getId());
            Consultation consultationIdOld = persistentRelevantMat.getConsultationId();
            Consultation consultationIdNew = relevantMat.getConsultationId();
            if (consultationIdNew != null) {
                consultationIdNew = em.getReference(consultationIdNew.getClass(), consultationIdNew.getId());
                relevantMat.setConsultationId(consultationIdNew);
            }
            relevantMat = em.merge(relevantMat);
            if (consultationIdOld != null && !consultationIdOld.equals(consultationIdNew)) {
                consultationIdOld.getRelevantMatCollection().remove(relevantMat);
                consultationIdOld = em.merge(consultationIdOld);
            }
            if (consultationIdNew != null && !consultationIdNew.equals(consultationIdOld)) {
                consultationIdNew.getRelevantMatCollection().add(relevantMat);
                consultationIdNew = em.merge(consultationIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = relevantMat.getId();
                if (findRelevantMat(id) == null) {
                    throw new NonexistentEntityException("The relevantMat with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RelevantMat relevantMat;
            try {
                relevantMat = em.getReference(RelevantMat.class, id);
                relevantMat.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The relevantMat with id " + id + " no longer exists.", enfe);
            }
            Consultation consultationId = relevantMat.getConsultationId();
            if (consultationId != null) {
                consultationId.getRelevantMatCollection().remove(relevantMat);
                consultationId = em.merge(consultationId);
            }
            em.remove(relevantMat);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RelevantMat> findRelevantMatEntities() {
        return findRelevantMatEntities(true, -1, -1);
    }

    public List<RelevantMat> findRelevantMatEntities(int maxResults, int firstResult) {
        return findRelevantMatEntities(false, maxResults, firstResult);
    }

    private List<RelevantMat> findRelevantMatEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RelevantMat.class));
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

    public RelevantMat findRelevantMat(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RelevantMat.class, id);
        } finally {
            em.close();
        }
    }

    public int getRelevantMatCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RelevantMat> rt = cq.from(RelevantMat.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
