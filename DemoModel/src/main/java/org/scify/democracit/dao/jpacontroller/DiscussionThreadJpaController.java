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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.DiscussionThread;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
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
        if (discussionThread.getCommentsCollection() == null) {
            discussionThread.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : discussionThread.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            discussionThread.setCommentsCollection(attachedCommentsCollection);
            em.persist(discussionThread);
            for (Comments commentsCollectionComments : discussionThread.getCommentsCollection()) {
                DiscussionThread oldDiscussionThreadIdOfCommentsCollectionComments = commentsCollectionComments.getDiscussionThreadId();
                commentsCollectionComments.setDiscussionThreadId(discussionThread);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldDiscussionThreadIdOfCommentsCollectionComments != null) {
                    oldDiscussionThreadIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldDiscussionThreadIdOfCommentsCollectionComments = em.merge(oldDiscussionThreadIdOfCommentsCollectionComments);
                }
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
            Collection<Comments> commentsCollectionOld = persistentDiscussionThread.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = discussionThread.getCommentsCollection();
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            discussionThread.setCommentsCollection(commentsCollectionNew);
            discussionThread = em.merge(discussionThread);
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    commentsCollectionOldComments.setDiscussionThreadId(null);
                    commentsCollectionOldComments = em.merge(commentsCollectionOldComments);
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    DiscussionThread oldDiscussionThreadIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getDiscussionThreadId();
                    commentsCollectionNewComments.setDiscussionThreadId(discussionThread);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldDiscussionThreadIdOfCommentsCollectionNewComments != null && !oldDiscussionThreadIdOfCommentsCollectionNewComments.equals(discussionThread)) {
                        oldDiscussionThreadIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldDiscussionThreadIdOfCommentsCollectionNewComments = em.merge(oldDiscussionThreadIdOfCommentsCollectionNewComments);
                    }
                }
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
            Collection<Comments> commentsCollection = discussionThread.getCommentsCollection();
            for (Comments commentsCollectionComments : commentsCollection) {
                commentsCollectionComments.setDiscussionThreadId(null);
                commentsCollectionComments = em.merge(commentsCollectionComments);
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
