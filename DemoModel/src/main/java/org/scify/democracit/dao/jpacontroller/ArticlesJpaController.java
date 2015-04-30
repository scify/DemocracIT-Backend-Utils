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
import org.scify.democracit.dao.model.Comments;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.Articles;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ArticlesJpaController implements Serializable {

    public ArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articles articles) {
        if (articles.getCommentsCollection() == null) {
            articles.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultation consultationId = articles.getConsultationId();
            if (consultationId != null) {
                consultationId = em.getReference(consultationId.getClass(), consultationId.getId());
                articles.setConsultationId(consultationId);
            }
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : articles.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            articles.setCommentsCollection(attachedCommentsCollection);
            em.persist(articles);
            if (consultationId != null) {
                consultationId.getArticlesCollection().add(articles);
                consultationId = em.merge(consultationId);
            }
            for (Comments commentsCollectionComments : articles.getCommentsCollection()) {
                Articles oldArticleIdOfCommentsCollectionComments = commentsCollectionComments.getArticleId();
                commentsCollectionComments.setArticleId(articles);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldArticleIdOfCommentsCollectionComments != null) {
                    oldArticleIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldArticleIdOfCommentsCollectionComments = em.merge(oldArticleIdOfCommentsCollectionComments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articles articles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articles persistentArticles = em.find(Articles.class, articles.getId());
            Consultation consultationIdOld = persistentArticles.getConsultationId();
            Consultation consultationIdNew = articles.getConsultationId();
            Collection<Comments> commentsCollectionOld = persistentArticles.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = articles.getCommentsCollection();
            List<String> illegalOrphanMessages = null;
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comments " + commentsCollectionOldComments + " since its articleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (consultationIdNew != null) {
                consultationIdNew = em.getReference(consultationIdNew.getClass(), consultationIdNew.getId());
                articles.setConsultationId(consultationIdNew);
            }
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            articles.setCommentsCollection(commentsCollectionNew);
            articles = em.merge(articles);
            if (consultationIdOld != null && !consultationIdOld.equals(consultationIdNew)) {
                consultationIdOld.getArticlesCollection().remove(articles);
                consultationIdOld = em.merge(consultationIdOld);
            }
            if (consultationIdNew != null && !consultationIdNew.equals(consultationIdOld)) {
                consultationIdNew.getArticlesCollection().add(articles);
                consultationIdNew = em.merge(consultationIdNew);
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Articles oldArticleIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getArticleId();
                    commentsCollectionNewComments.setArticleId(articles);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldArticleIdOfCommentsCollectionNewComments != null && !oldArticleIdOfCommentsCollectionNewComments.equals(articles)) {
                        oldArticleIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldArticleIdOfCommentsCollectionNewComments = em.merge(oldArticleIdOfCommentsCollectionNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = articles.getId();
                if (findArticles(id) == null) {
                    throw new NonexistentEntityException("The articles with id " + id + " no longer exists.");
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
            Articles articles;
            try {
                articles = em.getReference(Articles.class, id);
                articles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Comments> commentsCollectionOrphanCheck = articles.getCommentsCollection();
            for (Comments commentsCollectionOrphanCheckComments : commentsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articles (" + articles + ") cannot be destroyed since the Comments " + commentsCollectionOrphanCheckComments + " in its commentsCollection field has a non-nullable articleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Consultation consultationId = articles.getConsultationId();
            if (consultationId != null) {
                consultationId.getArticlesCollection().remove(articles);
                consultationId = em.merge(consultationId);
            }
            em.remove(articles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articles> findArticlesEntities() {
        return findArticlesEntities(true, -1, -1);
    }

    public List<Articles> findArticlesEntities(int maxResults, int firstResult) {
        return findArticlesEntities(false, maxResults, firstResult);
    }

    private List<Articles> findArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articles.class));
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

    public Articles findArticles(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articles.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articles> rt = cq.from(Articles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
