/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.dao.jpacontroller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.scify.democracit.dao.model.Consultation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.OrganizationLkp;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class OrganizationLkpJpaController implements Serializable {

    public OrganizationLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrganizationLkp organizationLkp) {
        if (organizationLkp.getConsultationCollection() == null) {
            organizationLkp.setConsultationCollection(new ArrayList<Consultation>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Consultation> attachedConsultationCollection = new ArrayList<Consultation>();
            for (Consultation consultationCollectionConsultationToAttach : organizationLkp.getConsultationCollection()) {
                consultationCollectionConsultationToAttach = em.getReference(consultationCollectionConsultationToAttach.getClass(), consultationCollectionConsultationToAttach.getId());
                attachedConsultationCollection.add(consultationCollectionConsultationToAttach);
            }
            organizationLkp.setConsultationCollection(attachedConsultationCollection);
            em.persist(organizationLkp);
            for (Consultation consultationCollectionConsultation : organizationLkp.getConsultationCollection()) {
                OrganizationLkp oldOrganizationIdOfConsultationCollectionConsultation = consultationCollectionConsultation.getOrganizationId();
                consultationCollectionConsultation.setOrganizationId(organizationLkp);
                consultationCollectionConsultation = em.merge(consultationCollectionConsultation);
                if (oldOrganizationIdOfConsultationCollectionConsultation != null) {
                    oldOrganizationIdOfConsultationCollectionConsultation.getConsultationCollection().remove(consultationCollectionConsultation);
                    oldOrganizationIdOfConsultationCollectionConsultation = em.merge(oldOrganizationIdOfConsultationCollectionConsultation);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrganizationLkp organizationLkp) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrganizationLkp persistentOrganizationLkp = em.find(OrganizationLkp.class, organizationLkp.getId());
            Collection<Consultation> consultationCollectionOld = persistentOrganizationLkp.getConsultationCollection();
            Collection<Consultation> consultationCollectionNew = organizationLkp.getConsultationCollection();
            Collection<Consultation> attachedConsultationCollectionNew = new ArrayList<Consultation>();
            for (Consultation consultationCollectionNewConsultationToAttach : consultationCollectionNew) {
                consultationCollectionNewConsultationToAttach = em.getReference(consultationCollectionNewConsultationToAttach.getClass(), consultationCollectionNewConsultationToAttach.getId());
                attachedConsultationCollectionNew.add(consultationCollectionNewConsultationToAttach);
            }
            consultationCollectionNew = attachedConsultationCollectionNew;
            organizationLkp.setConsultationCollection(consultationCollectionNew);
            organizationLkp = em.merge(organizationLkp);
            for (Consultation consultationCollectionOldConsultation : consultationCollectionOld) {
                if (!consultationCollectionNew.contains(consultationCollectionOldConsultation)) {
                    consultationCollectionOldConsultation.setOrganizationId(null);
                    consultationCollectionOldConsultation = em.merge(consultationCollectionOldConsultation);
                }
            }
            for (Consultation consultationCollectionNewConsultation : consultationCollectionNew) {
                if (!consultationCollectionOld.contains(consultationCollectionNewConsultation)) {
                    OrganizationLkp oldOrganizationIdOfConsultationCollectionNewConsultation = consultationCollectionNewConsultation.getOrganizationId();
                    consultationCollectionNewConsultation.setOrganizationId(organizationLkp);
                    consultationCollectionNewConsultation = em.merge(consultationCollectionNewConsultation);
                    if (oldOrganizationIdOfConsultationCollectionNewConsultation != null && !oldOrganizationIdOfConsultationCollectionNewConsultation.equals(organizationLkp)) {
                        oldOrganizationIdOfConsultationCollectionNewConsultation.getConsultationCollection().remove(consultationCollectionNewConsultation);
                        oldOrganizationIdOfConsultationCollectionNewConsultation = em.merge(oldOrganizationIdOfConsultationCollectionNewConsultation);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = organizationLkp.getId();
                if (findOrganizationLkp(id) == null) {
                    throw new NonexistentEntityException("The organizationLkp with id " + id + " no longer exists.");
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
            OrganizationLkp organizationLkp;
            try {
                organizationLkp = em.getReference(OrganizationLkp.class, id);
                organizationLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The organizationLkp with id " + id + " no longer exists.", enfe);
            }
            Collection<Consultation> consultationCollection = organizationLkp.getConsultationCollection();
            for (Consultation consultationCollectionConsultation : consultationCollection) {
                consultationCollectionConsultation.setOrganizationId(null);
                consultationCollectionConsultation = em.merge(consultationCollectionConsultation);
            }
            em.remove(organizationLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrganizationLkp> findOrganizationLkpEntities() {
        return findOrganizationLkpEntities(true, -1, -1);
    }

    public List<OrganizationLkp> findOrganizationLkpEntities(int maxResults, int firstResult) {
        return findOrganizationLkpEntities(false, maxResults, firstResult);
    }

    private List<OrganizationLkp> findOrganizationLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrganizationLkp.class));
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

    public OrganizationLkp findOrganizationLkp(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrganizationLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrganizationLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrganizationLkp> rt = cq.from(OrganizationLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
