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
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.CommentTerm;
import org.scify.democracit.dao.model.Comments;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class CommentTermJpaController implements Serializable {

    public CommentTermJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommentTerm commentTerm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments commentId = commentTerm.getCommentId();
            if (commentId != null) {
                commentId = em.getReference(commentId.getClass(), commentId.getId());
                commentTerm.setCommentId(commentId);
            }
            em.persist(commentTerm);
            if (commentId != null) {
                commentId.getCommentTermCollection().add(commentTerm);
                commentId = em.merge(commentId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CommentTerm commentTerm) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommentTerm persistentCommentTerm = em.find(CommentTerm.class, commentTerm.getId());
            Comments commentIdOld = persistentCommentTerm.getCommentId();
            Comments commentIdNew = commentTerm.getCommentId();
            if (commentIdNew != null) {
                commentIdNew = em.getReference(commentIdNew.getClass(), commentIdNew.getId());
                commentTerm.setCommentId(commentIdNew);
            }
            commentTerm = em.merge(commentTerm);
            if (commentIdOld != null && !commentIdOld.equals(commentIdNew)) {
                commentIdOld.getCommentTermCollection().remove(commentTerm);
                commentIdOld = em.merge(commentIdOld);
            }
            if (commentIdNew != null && !commentIdNew.equals(commentIdOld)) {
                commentIdNew.getCommentTermCollection().add(commentTerm);
                commentIdNew = em.merge(commentIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = commentTerm.getId();
                if (findCommentTerm(id) == null) {
                    throw new NonexistentEntityException("The commentTerm with id " + id + " no longer exists.");
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
            CommentTerm commentTerm;
            try {
                commentTerm = em.getReference(CommentTerm.class, id);
                commentTerm.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commentTerm with id " + id + " no longer exists.", enfe);
            }
            Comments commentId = commentTerm.getCommentId();
            if (commentId != null) {
                commentId.getCommentTermCollection().remove(commentTerm);
                commentId = em.merge(commentId);
            }
            em.remove(commentTerm);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CommentTerm> findCommentTermEntities() {
        return findCommentTermEntities(true, -1, -1);
    }

    public List<CommentTerm> findCommentTermEntities(int maxResults, int firstResult) {
        return findCommentTermEntities(false, maxResults, firstResult);
    }

    private List<CommentTerm> findCommentTermEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommentTerm.class));
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

    public CommentTerm findCommentTerm(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommentTerm.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentTermCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommentTerm> rt = cq.from(CommentTerm.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public CommentTerm findByTermStringAndCommentID(String term, long commentID) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<CommentTerm> query
                    = em.createNamedQuery("CommentTerm.findByTermStringAndCommentID", CommentTerm.class);
            query.setParameter("termString", term);
            query.setParameter("commentId", new Comments(commentID));
            CommentTerm result = query.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

}
