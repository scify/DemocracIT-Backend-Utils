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
import org.scify.democracit.dao.model.ConsultationSkipList;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ConsultationSkipListJpaController implements Serializable {

    public ConsultationSkipListJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConsultationSkipList consultationSkipList) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(consultationSkipList);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConsultationSkipList consultationSkipList) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            consultationSkipList = em.merge(consultationSkipList);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = consultationSkipList.getId();
                if (findConsultationSkipList(id) == null) {
                    throw new NonexistentEntityException("The consultationSkipList with id " + id + " no longer exists.");
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
            ConsultationSkipList consultationSkipList;
            try {
                consultationSkipList = em.getReference(ConsultationSkipList.class, id);
                consultationSkipList.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultationSkipList with id " + id + " no longer exists.", enfe);
            }
            em.remove(consultationSkipList);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConsultationSkipList> findConsultationSkipListEntities() {
        return findConsultationSkipListEntities(true, -1, -1);
    }

    public List<ConsultationSkipList> findConsultationSkipListEntities(int maxResults, int firstResult) {
        return findConsultationSkipListEntities(false, maxResults, firstResult);
    }

    private List<ConsultationSkipList> findConsultationSkipListEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConsultationSkipList.class));
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

    public ConsultationSkipList findConsultationSkipList(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConsultationSkipList.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultationSkipListCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConsultationSkipList> rt = cq.from(ConsultationSkipList.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
