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
import org.scify.democracit.dao.model.Comments;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.jpacontroller.exceptions.PreexistingEntityException;
import org.scify.democracit.dao.model.CommentOpengov;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class CommentOpengovJpaController implements Serializable {

    public CommentOpengovJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommentOpengov commentOpengov) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Comments commentsOrphanCheck = commentOpengov.getComments();
        if (commentsOrphanCheck != null) {
            CommentOpengov oldCommentOpengovOfComments = commentsOrphanCheck.getCommentOpengov();
            if (oldCommentOpengovOfComments != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Comments " + commentsOrphanCheck + " already has an item of type CommentOpengov whose comments column cannot be null. Please make another selection for the comments field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments comments = commentOpengov.getComments();
            if (comments != null) {
                comments = em.getReference(comments.getClass(), comments.getId());
                commentOpengov.setComments(comments);
            }
            em.persist(commentOpengov);
            if (comments != null) {
                comments.setCommentOpengov(commentOpengov);
                comments = em.merge(comments);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCommentOpengov(commentOpengov.getId()) != null) {
                throw new PreexistingEntityException("CommentOpengov " + commentOpengov + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CommentOpengov commentOpengov) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommentOpengov persistentCommentOpengov = em.find(CommentOpengov.class, commentOpengov.getId());
            Comments commentsOld = persistentCommentOpengov.getComments();
            Comments commentsNew = commentOpengov.getComments();
            List<String> illegalOrphanMessages = null;
            if (commentsNew != null && !commentsNew.equals(commentsOld)) {
                CommentOpengov oldCommentOpengovOfComments = commentsNew.getCommentOpengov();
                if (oldCommentOpengovOfComments != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Comments " + commentsNew + " already has an item of type CommentOpengov whose comments column cannot be null. Please make another selection for the comments field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (commentsNew != null) {
                commentsNew = em.getReference(commentsNew.getClass(), commentsNew.getId());
                commentOpengov.setComments(commentsNew);
            }
            commentOpengov = em.merge(commentOpengov);
            if (commentsOld != null && !commentsOld.equals(commentsNew)) {
                commentsOld.setCommentOpengov(null);
                commentsOld = em.merge(commentsOld);
            }
            if (commentsNew != null && !commentsNew.equals(commentsOld)) {
                commentsNew.setCommentOpengov(commentOpengov);
                commentsNew = em.merge(commentsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = commentOpengov.getId();
                if (findCommentOpengov(id) == null) {
                    throw new NonexistentEntityException("The commentOpengov with id " + id + " no longer exists.");
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
            CommentOpengov commentOpengov;
            try {
                commentOpengov = em.getReference(CommentOpengov.class, id);
                commentOpengov.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commentOpengov with id " + id + " no longer exists.", enfe);
            }
            Comments comments = commentOpengov.getComments();
            if (comments != null) {
                comments.setCommentOpengov(null);
                comments = em.merge(comments);
            }
            em.remove(commentOpengov);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CommentOpengov> findCommentOpengovEntities() {
        return findCommentOpengovEntities(true, -1, -1);
    }

    public List<CommentOpengov> findCommentOpengovEntities(int maxResults, int firstResult) {
        return findCommentOpengovEntities(false, maxResults, firstResult);
    }

    private List<CommentOpengov> findCommentOpengovEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommentOpengov.class));
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

    public CommentOpengov findCommentOpengov(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommentOpengov.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentOpengovCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommentOpengov> rt = cq.from(CommentOpengov.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
