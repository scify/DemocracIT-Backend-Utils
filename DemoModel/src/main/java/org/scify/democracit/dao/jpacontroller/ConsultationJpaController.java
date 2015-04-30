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
import org.scify.democracit.dao.model.OrganizationLkp;
import org.scify.democracit.dao.model.RelevantMat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.Articles;
import org.scify.democracit.dao.model.Consultation;
import org.scify.democracit.dao.model.RelevantMaterial;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ConsultationJpaController implements Serializable {

    public ConsultationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consultation consultation) {
        if (consultation.getRelevantMatCollection() == null) {
            consultation.setRelevantMatCollection(new ArrayList<RelevantMat>());
        }
        if (consultation.getArticlesCollection() == null) {
            consultation.setArticlesCollection(new ArrayList<Articles>());
        }
        if (consultation.getRelevantMaterialCollection() == null) {
            consultation.setRelevantMaterialCollection(new ArrayList<RelevantMaterial>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrganizationLkp organizationId = consultation.getOrganizationId();
            if (organizationId != null) {
                organizationId = em.getReference(organizationId.getClass(), organizationId.getId());
                consultation.setOrganizationId(organizationId);
            }
            Collection<RelevantMat> attachedRelevantMatCollection = new ArrayList<RelevantMat>();
            for (RelevantMat relevantMatCollectionRelevantMatToAttach : consultation.getRelevantMatCollection()) {
                relevantMatCollectionRelevantMatToAttach = em.getReference(relevantMatCollectionRelevantMatToAttach.getClass(), relevantMatCollectionRelevantMatToAttach.getId());
                attachedRelevantMatCollection.add(relevantMatCollectionRelevantMatToAttach);
            }
            consultation.setRelevantMatCollection(attachedRelevantMatCollection);
            Collection<Articles> attachedArticlesCollection = new ArrayList<Articles>();
            for (Articles articlesCollectionArticlesToAttach : consultation.getArticlesCollection()) {
                articlesCollectionArticlesToAttach = em.getReference(articlesCollectionArticlesToAttach.getClass(), articlesCollectionArticlesToAttach.getId());
                attachedArticlesCollection.add(articlesCollectionArticlesToAttach);
            }
            consultation.setArticlesCollection(attachedArticlesCollection);
            Collection<RelevantMaterial> attachedRelevantMaterialCollection = new ArrayList<RelevantMaterial>();
            for (RelevantMaterial relevantMaterialCollectionRelevantMaterialToAttach : consultation.getRelevantMaterialCollection()) {
                relevantMaterialCollectionRelevantMaterialToAttach = em.getReference(relevantMaterialCollectionRelevantMaterialToAttach.getClass(), relevantMaterialCollectionRelevantMaterialToAttach.getId());
                attachedRelevantMaterialCollection.add(relevantMaterialCollectionRelevantMaterialToAttach);
            }
            consultation.setRelevantMaterialCollection(attachedRelevantMaterialCollection);
            em.persist(consultation);
            if (organizationId != null) {
                organizationId.getConsultationCollection().add(consultation);
                organizationId = em.merge(organizationId);
            }
            for (RelevantMat relevantMatCollectionRelevantMat : consultation.getRelevantMatCollection()) {
                Consultation oldConsultationIdOfRelevantMatCollectionRelevantMat = relevantMatCollectionRelevantMat.getConsultationId();
                relevantMatCollectionRelevantMat.setConsultationId(consultation);
                relevantMatCollectionRelevantMat = em.merge(relevantMatCollectionRelevantMat);
                if (oldConsultationIdOfRelevantMatCollectionRelevantMat != null) {
                    oldConsultationIdOfRelevantMatCollectionRelevantMat.getRelevantMatCollection().remove(relevantMatCollectionRelevantMat);
                    oldConsultationIdOfRelevantMatCollectionRelevantMat = em.merge(oldConsultationIdOfRelevantMatCollectionRelevantMat);
                }
            }
            for (Articles articlesCollectionArticles : consultation.getArticlesCollection()) {
                Consultation oldConsultationIdOfArticlesCollectionArticles = articlesCollectionArticles.getConsultationId();
                articlesCollectionArticles.setConsultationId(consultation);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
                if (oldConsultationIdOfArticlesCollectionArticles != null) {
                    oldConsultationIdOfArticlesCollectionArticles.getArticlesCollection().remove(articlesCollectionArticles);
                    oldConsultationIdOfArticlesCollectionArticles = em.merge(oldConsultationIdOfArticlesCollectionArticles);
                }
            }
            for (RelevantMaterial relevantMaterialCollectionRelevantMaterial : consultation.getRelevantMaterialCollection()) {
                Consultation oldConsultationIdOfRelevantMaterialCollectionRelevantMaterial = relevantMaterialCollectionRelevantMaterial.getConsultationId();
                relevantMaterialCollectionRelevantMaterial.setConsultationId(consultation);
                relevantMaterialCollectionRelevantMaterial = em.merge(relevantMaterialCollectionRelevantMaterial);
                if (oldConsultationIdOfRelevantMaterialCollectionRelevantMaterial != null) {
                    oldConsultationIdOfRelevantMaterialCollectionRelevantMaterial.getRelevantMaterialCollection().remove(relevantMaterialCollectionRelevantMaterial);
                    oldConsultationIdOfRelevantMaterialCollectionRelevantMaterial = em.merge(oldConsultationIdOfRelevantMaterialCollectionRelevantMaterial);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consultation consultation) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultation persistentConsultation = em.find(Consultation.class, consultation.getId());
            OrganizationLkp organizationIdOld = persistentConsultation.getOrganizationId();
            OrganizationLkp organizationIdNew = consultation.getOrganizationId();
            Collection<RelevantMat> relevantMatCollectionOld = persistentConsultation.getRelevantMatCollection();
            Collection<RelevantMat> relevantMatCollectionNew = consultation.getRelevantMatCollection();
            Collection<Articles> articlesCollectionOld = persistentConsultation.getArticlesCollection();
            Collection<Articles> articlesCollectionNew = consultation.getArticlesCollection();
            Collection<RelevantMaterial> relevantMaterialCollectionOld = persistentConsultation.getRelevantMaterialCollection();
            Collection<RelevantMaterial> relevantMaterialCollectionNew = consultation.getRelevantMaterialCollection();
            List<String> illegalOrphanMessages = null;
            for (RelevantMat relevantMatCollectionOldRelevantMat : relevantMatCollectionOld) {
                if (!relevantMatCollectionNew.contains(relevantMatCollectionOldRelevantMat)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RelevantMat " + relevantMatCollectionOldRelevantMat + " since its consultationId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (organizationIdNew != null) {
                organizationIdNew = em.getReference(organizationIdNew.getClass(), organizationIdNew.getId());
                consultation.setOrganizationId(organizationIdNew);
            }
            Collection<RelevantMat> attachedRelevantMatCollectionNew = new ArrayList<RelevantMat>();
            for (RelevantMat relevantMatCollectionNewRelevantMatToAttach : relevantMatCollectionNew) {
                relevantMatCollectionNewRelevantMatToAttach = em.getReference(relevantMatCollectionNewRelevantMatToAttach.getClass(), relevantMatCollectionNewRelevantMatToAttach.getId());
                attachedRelevantMatCollectionNew.add(relevantMatCollectionNewRelevantMatToAttach);
            }
            relevantMatCollectionNew = attachedRelevantMatCollectionNew;
            consultation.setRelevantMatCollection(relevantMatCollectionNew);
            Collection<Articles> attachedArticlesCollectionNew = new ArrayList<Articles>();
            for (Articles articlesCollectionNewArticlesToAttach : articlesCollectionNew) {
                articlesCollectionNewArticlesToAttach = em.getReference(articlesCollectionNewArticlesToAttach.getClass(), articlesCollectionNewArticlesToAttach.getId());
                attachedArticlesCollectionNew.add(articlesCollectionNewArticlesToAttach);
            }
            articlesCollectionNew = attachedArticlesCollectionNew;
            consultation.setArticlesCollection(articlesCollectionNew);
            Collection<RelevantMaterial> attachedRelevantMaterialCollectionNew = new ArrayList<RelevantMaterial>();
            for (RelevantMaterial relevantMaterialCollectionNewRelevantMaterialToAttach : relevantMaterialCollectionNew) {
                relevantMaterialCollectionNewRelevantMaterialToAttach = em.getReference(relevantMaterialCollectionNewRelevantMaterialToAttach.getClass(), relevantMaterialCollectionNewRelevantMaterialToAttach.getId());
                attachedRelevantMaterialCollectionNew.add(relevantMaterialCollectionNewRelevantMaterialToAttach);
            }
            relevantMaterialCollectionNew = attachedRelevantMaterialCollectionNew;
            consultation.setRelevantMaterialCollection(relevantMaterialCollectionNew);
            consultation = em.merge(consultation);
            if (organizationIdOld != null && !organizationIdOld.equals(organizationIdNew)) {
                organizationIdOld.getConsultationCollection().remove(consultation);
                organizationIdOld = em.merge(organizationIdOld);
            }
            if (organizationIdNew != null && !organizationIdNew.equals(organizationIdOld)) {
                organizationIdNew.getConsultationCollection().add(consultation);
                organizationIdNew = em.merge(organizationIdNew);
            }
            for (RelevantMat relevantMatCollectionNewRelevantMat : relevantMatCollectionNew) {
                if (!relevantMatCollectionOld.contains(relevantMatCollectionNewRelevantMat)) {
                    Consultation oldConsultationIdOfRelevantMatCollectionNewRelevantMat = relevantMatCollectionNewRelevantMat.getConsultationId();
                    relevantMatCollectionNewRelevantMat.setConsultationId(consultation);
                    relevantMatCollectionNewRelevantMat = em.merge(relevantMatCollectionNewRelevantMat);
                    if (oldConsultationIdOfRelevantMatCollectionNewRelevantMat != null && !oldConsultationIdOfRelevantMatCollectionNewRelevantMat.equals(consultation)) {
                        oldConsultationIdOfRelevantMatCollectionNewRelevantMat.getRelevantMatCollection().remove(relevantMatCollectionNewRelevantMat);
                        oldConsultationIdOfRelevantMatCollectionNewRelevantMat = em.merge(oldConsultationIdOfRelevantMatCollectionNewRelevantMat);
                    }
                }
            }
            for (Articles articlesCollectionOldArticles : articlesCollectionOld) {
                if (!articlesCollectionNew.contains(articlesCollectionOldArticles)) {
                    articlesCollectionOldArticles.setConsultationId(null);
                    articlesCollectionOldArticles = em.merge(articlesCollectionOldArticles);
                }
            }
            for (Articles articlesCollectionNewArticles : articlesCollectionNew) {
                if (!articlesCollectionOld.contains(articlesCollectionNewArticles)) {
                    Consultation oldConsultationIdOfArticlesCollectionNewArticles = articlesCollectionNewArticles.getConsultationId();
                    articlesCollectionNewArticles.setConsultationId(consultation);
                    articlesCollectionNewArticles = em.merge(articlesCollectionNewArticles);
                    if (oldConsultationIdOfArticlesCollectionNewArticles != null && !oldConsultationIdOfArticlesCollectionNewArticles.equals(consultation)) {
                        oldConsultationIdOfArticlesCollectionNewArticles.getArticlesCollection().remove(articlesCollectionNewArticles);
                        oldConsultationIdOfArticlesCollectionNewArticles = em.merge(oldConsultationIdOfArticlesCollectionNewArticles);
                    }
                }
            }
            for (RelevantMaterial relevantMaterialCollectionOldRelevantMaterial : relevantMaterialCollectionOld) {
                if (!relevantMaterialCollectionNew.contains(relevantMaterialCollectionOldRelevantMaterial)) {
                    relevantMaterialCollectionOldRelevantMaterial.setConsultationId(null);
                    relevantMaterialCollectionOldRelevantMaterial = em.merge(relevantMaterialCollectionOldRelevantMaterial);
                }
            }
            for (RelevantMaterial relevantMaterialCollectionNewRelevantMaterial : relevantMaterialCollectionNew) {
                if (!relevantMaterialCollectionOld.contains(relevantMaterialCollectionNewRelevantMaterial)) {
                    Consultation oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial = relevantMaterialCollectionNewRelevantMaterial.getConsultationId();
                    relevantMaterialCollectionNewRelevantMaterial.setConsultationId(consultation);
                    relevantMaterialCollectionNewRelevantMaterial = em.merge(relevantMaterialCollectionNewRelevantMaterial);
                    if (oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial != null && !oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial.equals(consultation)) {
                        oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial.getRelevantMaterialCollection().remove(relevantMaterialCollectionNewRelevantMaterial);
                        oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial = em.merge(oldConsultationIdOfRelevantMaterialCollectionNewRelevantMaterial);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = consultation.getId();
                if (findConsultation(id) == null) {
                    throw new NonexistentEntityException("The consultation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultation consultation;
            try {
                consultation = em.getReference(Consultation.class, id);
                consultation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultation with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RelevantMat> relevantMatCollectionOrphanCheck = consultation.getRelevantMatCollection();
            for (RelevantMat relevantMatCollectionOrphanCheckRelevantMat : relevantMatCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consultation (" + consultation + ") cannot be destroyed since the RelevantMat " + relevantMatCollectionOrphanCheckRelevantMat + " in its relevantMatCollection field has a non-nullable consultationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            OrganizationLkp organizationId = consultation.getOrganizationId();
            if (organizationId != null) {
                organizationId.getConsultationCollection().remove(consultation);
                organizationId = em.merge(organizationId);
            }
            Collection<Articles> articlesCollection = consultation.getArticlesCollection();
            for (Articles articlesCollectionArticles : articlesCollection) {
                articlesCollectionArticles.setConsultationId(null);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
            }
            Collection<RelevantMaterial> relevantMaterialCollection = consultation.getRelevantMaterialCollection();
            for (RelevantMaterial relevantMaterialCollectionRelevantMaterial : relevantMaterialCollection) {
                relevantMaterialCollectionRelevantMaterial.setConsultationId(null);
                relevantMaterialCollectionRelevantMaterial = em.merge(relevantMaterialCollectionRelevantMaterial);
            }
            em.remove(consultation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consultation> findConsultationEntities() {
        return findConsultationEntities(true, -1, -1);
    }

    public List<Consultation> findConsultationEntities(int maxResults, int firstResult) {
        return findConsultationEntities(false, maxResults, firstResult);
    }

    private List<Consultation> findConsultationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consultation.class));
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

    public Consultation findConsultation(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consultation.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consultation> rt = cq.from(Consultation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
