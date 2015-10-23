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
import org.scify.democracit.dao.model.DiscussionThread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.jpacontroller.exceptions.PreexistingEntityException;
import org.scify.democracit.dao.model.DiscussionThreadTypes;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class DiscussionThreadTypesJpaController implements Serializable {

    public DiscussionThreadTypesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscussionThreadTypes discussionThreadTypes) throws PreexistingEntityException, Exception {
        if (discussionThreadTypes.getDiscussionThreadCollection() == null) {
            discussionThreadTypes.setDiscussionThreadCollection(new ArrayList<DiscussionThread>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<DiscussionThread> attachedDiscussionThreadCollection = new ArrayList<DiscussionThread>();
            for (DiscussionThread discussionThreadCollectionDiscussionThreadToAttach : discussionThreadTypes.getDiscussionThreadCollection()) {
                discussionThreadCollectionDiscussionThreadToAttach = em.getReference(discussionThreadCollectionDiscussionThreadToAttach.getClass(), discussionThreadCollectionDiscussionThreadToAttach.getId());
                attachedDiscussionThreadCollection.add(discussionThreadCollectionDiscussionThreadToAttach);
            }
            discussionThreadTypes.setDiscussionThreadCollection(attachedDiscussionThreadCollection);
            em.persist(discussionThreadTypes);
            for (DiscussionThread discussionThreadCollectionDiscussionThread : discussionThreadTypes.getDiscussionThreadCollection()) {
                DiscussionThreadTypes oldTypeidOfDiscussionThreadCollectionDiscussionThread = discussionThreadCollectionDiscussionThread.getTypeid();
                discussionThreadCollectionDiscussionThread.setTypeid(discussionThreadTypes);
                discussionThreadCollectionDiscussionThread = em.merge(discussionThreadCollectionDiscussionThread);
                if (oldTypeidOfDiscussionThreadCollectionDiscussionThread != null) {
                    oldTypeidOfDiscussionThreadCollectionDiscussionThread.getDiscussionThreadCollection().remove(discussionThreadCollectionDiscussionThread);
                    oldTypeidOfDiscussionThreadCollectionDiscussionThread = em.merge(oldTypeidOfDiscussionThreadCollectionDiscussionThread);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiscussionThreadTypes(discussionThreadTypes.getId()) != null) {
                throw new PreexistingEntityException("DiscussionThreadTypes " + discussionThreadTypes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiscussionThreadTypes discussionThreadTypes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionThreadTypes persistentDiscussionThreadTypes = em.find(DiscussionThreadTypes.class, discussionThreadTypes.getId());
            Collection<DiscussionThread> discussionThreadCollectionOld = persistentDiscussionThreadTypes.getDiscussionThreadCollection();
            Collection<DiscussionThread> discussionThreadCollectionNew = discussionThreadTypes.getDiscussionThreadCollection();
            List<String> illegalOrphanMessages = null;
            for (DiscussionThread discussionThreadCollectionOldDiscussionThread : discussionThreadCollectionOld) {
                if (!discussionThreadCollectionNew.contains(discussionThreadCollectionOldDiscussionThread)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiscussionThread " + discussionThreadCollectionOldDiscussionThread + " since its typeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DiscussionThread> attachedDiscussionThreadCollectionNew = new ArrayList<DiscussionThread>();
            for (DiscussionThread discussionThreadCollectionNewDiscussionThreadToAttach : discussionThreadCollectionNew) {
                discussionThreadCollectionNewDiscussionThreadToAttach = em.getReference(discussionThreadCollectionNewDiscussionThreadToAttach.getClass(), discussionThreadCollectionNewDiscussionThreadToAttach.getId());
                attachedDiscussionThreadCollectionNew.add(discussionThreadCollectionNewDiscussionThreadToAttach);
            }
            discussionThreadCollectionNew = attachedDiscussionThreadCollectionNew;
            discussionThreadTypes.setDiscussionThreadCollection(discussionThreadCollectionNew);
            discussionThreadTypes = em.merge(discussionThreadTypes);
            for (DiscussionThread discussionThreadCollectionNewDiscussionThread : discussionThreadCollectionNew) {
                if (!discussionThreadCollectionOld.contains(discussionThreadCollectionNewDiscussionThread)) {
                    DiscussionThreadTypes oldTypeidOfDiscussionThreadCollectionNewDiscussionThread = discussionThreadCollectionNewDiscussionThread.getTypeid();
                    discussionThreadCollectionNewDiscussionThread.setTypeid(discussionThreadTypes);
                    discussionThreadCollectionNewDiscussionThread = em.merge(discussionThreadCollectionNewDiscussionThread);
                    if (oldTypeidOfDiscussionThreadCollectionNewDiscussionThread != null && !oldTypeidOfDiscussionThreadCollectionNewDiscussionThread.equals(discussionThreadTypes)) {
                        oldTypeidOfDiscussionThreadCollectionNewDiscussionThread.getDiscussionThreadCollection().remove(discussionThreadCollectionNewDiscussionThread);
                        oldTypeidOfDiscussionThreadCollectionNewDiscussionThread = em.merge(oldTypeidOfDiscussionThreadCollectionNewDiscussionThread);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = discussionThreadTypes.getId();
                if (findDiscussionThreadTypes(id) == null) {
                    throw new NonexistentEntityException("The discussionThreadTypes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiscussionThreadTypes discussionThreadTypes;
            try {
                discussionThreadTypes = em.getReference(DiscussionThreadTypes.class, id);
                discussionThreadTypes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discussionThreadTypes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DiscussionThread> discussionThreadCollectionOrphanCheck = discussionThreadTypes.getDiscussionThreadCollection();
            for (DiscussionThread discussionThreadCollectionOrphanCheckDiscussionThread : discussionThreadCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiscussionThreadTypes (" + discussionThreadTypes + ") cannot be destroyed since the DiscussionThread " + discussionThreadCollectionOrphanCheckDiscussionThread + " in its discussionThreadCollection field has a non-nullable typeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(discussionThreadTypes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiscussionThreadTypes> findDiscussionThreadTypesEntities() {
        return findDiscussionThreadTypesEntities(true, -1, -1);
    }

    public List<DiscussionThreadTypes> findDiscussionThreadTypesEntities(int maxResults, int firstResult) {
        return findDiscussionThreadTypesEntities(false, maxResults, firstResult);
    }

    private List<DiscussionThreadTypes> findDiscussionThreadTypesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscussionThreadTypes.class));
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

    public DiscussionThreadTypes findDiscussionThreadTypes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscussionThreadTypes.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscussionThreadTypesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscussionThreadTypes> rt = cq.from(DiscussionThreadTypes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
