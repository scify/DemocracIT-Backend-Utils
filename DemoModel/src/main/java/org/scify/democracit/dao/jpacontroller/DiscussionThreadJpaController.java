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
import org.scify.democracit.dao.model.DiscussionThread;
import org.scify.democracit.dao.model.DiscussionThreadTypes;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class DiscussionThreadJpaController implements Serializable {

    public DiscussionThreadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionThread discussionThread) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionThreadTypes typeid = discussionThread.getTypeid();
            if (typeid != null) {
                typeid = em.getReference(typeid.getClass(), typeid.getId());
                discussionThread.setTypeid(typeid);
            }
            em.persist(discussionThread);
            if (typeid != null) {
                typeid.getDiscussionThreadCollection().add(discussionThread);
                typeid = em.merge(typeid);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiscussionThread discussionThread) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionThread persistentDiscussionThread = em.find(DiscussionThread.class, discussionThread.getId());
            DiscussionThreadTypes typeidOld = persistentDiscussionThread.getTypeid();
            DiscussionThreadTypes typeidNew = discussionThread.getTypeid();
            if (typeidNew != null) {
                typeidNew = em.getReference(typeidNew.getClass(), typeidNew.getId());
                discussionThread.setTypeid(typeidNew);
            }
            discussionThread = em.merge(discussionThread);
            if (typeidOld != null && !typeidOld.equals(typeidNew)) {
                typeidOld.getDiscussionThreadCollection().remove(discussionThread);
                typeidOld = em.merge(typeidOld);
            }
            if (typeidNew != null && !typeidNew.equals(typeidOld)) {
                typeidNew.getDiscussionThreadCollection().add(discussionThread);
                typeidNew = em.merge(typeidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = discussionThread.getId();
                if (findDiscussionThread(id) == null) {
                    throw new NonexistentEntityException("The discussionThread with id " + id + " no longer exists.");
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
            DiscussionThread discussionThread;
            try {
                discussionThread = em.getReference(DiscussionThread.class, id);
                discussionThread.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionThread with id " + id + " no longer exists.", enfe);
            }
            DiscussionThreadTypes typeid = discussionThread.getTypeid();
            if (typeid != null) {
                typeid.getDiscussionThreadCollection().remove(discussionThread);
                typeid = em.merge(typeid);
            }
            em.remove(discussionThread);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiscussionThread> findDiscussionThreadEntities() {
        return findDiscussionThreadEntities(true, -1, -1);
    }

    public List<DiscussionThread> findDiscussionThreadEntities(int maxResults, int firstResult) {
        return findDiscussionThreadEntities(false, maxResults, firstResult);
    }

    private List<DiscussionThread> findDiscussionThreadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionThread.class));
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

    public DiscussionThread findDiscussionThread(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionThread.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionThreadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionThread> rt = cq.from(DiscussionThread.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
