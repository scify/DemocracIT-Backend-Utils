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
import org.scify.democracit.dao.model.RelevantMaterial;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class RelevantMaterialJpaController implements Serializable {

    public RelevantMaterialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RelevantMaterial relevantMaterial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultation consultationId = relevantMaterial.getConsultationId();
            if (consultationId != null) {
                consultationId = em.getReference(consultationId.getClass(), consultationId.getId());
                relevantMaterial.setConsultationId(consultationId);
            }
            em.persist(relevantMaterial);
            if (consultationId != null) {
                consultationId.getRelevantMaterialCollection().add(relevantMaterial);
                consultationId = em.merge(consultationId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RelevantMaterial relevantMaterial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RelevantMaterial persistentRelevantMaterial = em.find(RelevantMaterial.class, relevantMaterial.getId());
            Consultation consultationIdOld = persistentRelevantMaterial.getConsultationId();
            Consultation consultationIdNew = relevantMaterial.getConsultationId();
            if (consultationIdNew != null) {
                consultationIdNew = em.getReference(consultationIdNew.getClass(), consultationIdNew.getId());
                relevantMaterial.setConsultationId(consultationIdNew);
            }
            relevantMaterial = em.merge(relevantMaterial);
            if (consultationIdOld != null && !consultationIdOld.equals(consultationIdNew)) {
                consultationIdOld.getRelevantMaterialCollection().remove(relevantMaterial);
                consultationIdOld = em.merge(consultationIdOld);
            }
            if (consultationIdNew != null && !consultationIdNew.equals(consultationIdOld)) {
                consultationIdNew.getRelevantMaterialCollection().add(relevantMaterial);
                consultationIdNew = em.merge(consultationIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = relevantMaterial.getId();
                if (findRelevantMaterial(id) == null) {
                    throw new NonexistentEntityException("The relevantMaterial with id " + id + " no longer exists.");
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
            RelevantMaterial relevantMaterial;
            try {
                relevantMaterial = em.getReference(RelevantMaterial.class, id);
                relevantMaterial.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The relevantMaterial with id " + id + " no longer exists.", enfe);
            }
            Consultation consultationId = relevantMaterial.getConsultationId();
            if (consultationId != null) {
                consultationId.getRelevantMaterialCollection().remove(relevantMaterial);
                consultationId = em.merge(consultationId);
            }
            em.remove(relevantMaterial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RelevantMaterial> findRelevantMaterialEntities() {
        return findRelevantMaterialEntities(true, -1, -1);
    }

    public List<RelevantMaterial> findRelevantMaterialEntities(int maxResults, int firstResult) {
        return findRelevantMaterialEntities(false, maxResults, firstResult);
    }

    private List<RelevantMaterial> findRelevantMaterialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RelevantMaterial.class));
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

    public RelevantMaterial findRelevantMaterial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RelevantMaterial.class, id);
        } finally {
            em.close();
        }
    }

    public int getRelevantMaterialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RelevantMaterial> rt = cq.from(RelevantMaterial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
