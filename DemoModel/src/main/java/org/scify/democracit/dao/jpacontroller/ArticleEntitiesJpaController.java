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
import org.scify.democracit.dao.model.ArticleEntities;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ArticleEntitiesJpaController implements Serializable {

    public ArticleEntitiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ArticleEntities articleEntities) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(articleEntities);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ArticleEntities articleEntities) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            articleEntities = em.merge(articleEntities);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articleEntities.getId();
                if (findArticleEntities(id) == null) {
                    throw new NonexistentEntityException("The articleEntities with id " + id + " no longer exists.");
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
            ArticleEntities articleEntities;
            try {
                articleEntities = em.getReference(ArticleEntities.class, id);
                articleEntities.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articleEntities with id " + id + " no longer exists.", enfe);
            }
            em.remove(articleEntities);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ArticleEntities> findArticleEntitiesEntities() {
        return findArticleEntitiesEntities(true, -1, -1);
    }

    public List<ArticleEntities> findArticleEntitiesEntities(int maxResults, int firstResult) {
        return findArticleEntitiesEntities(false, maxResults, firstResult);
    }

    private List<ArticleEntities> findArticleEntitiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArticleEntities.class));
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

    public ArticleEntities findArticleEntities(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ArticleEntities.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticleEntitiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArticleEntities> rt = cq.from(ArticleEntities.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
