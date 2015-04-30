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
import org.scify.democracit.dao.model.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.Comments;
import org.scify.democracit.dao.model.SourceTypeLkp;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class SourceTypeLkpJpaController implements Serializable {

    public SourceTypeLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SourceTypeLkp sourceTypeLkp) {
        if (sourceTypeLkp.getUsersCollection() == null) {
            sourceTypeLkp.setUsersCollection(new ArrayList<Users>());
        }
        if (sourceTypeLkp.getCommentsCollection() == null) {
            sourceTypeLkp.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : sourceTypeLkp.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getId());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            sourceTypeLkp.setUsersCollection(attachedUsersCollection);
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : sourceTypeLkp.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            sourceTypeLkp.setCommentsCollection(attachedCommentsCollection);
            em.persist(sourceTypeLkp);
            for (Users usersCollectionUsers : sourceTypeLkp.getUsersCollection()) {
                SourceTypeLkp oldSourceTypeIdOfUsersCollectionUsers = usersCollectionUsers.getSourceTypeId();
                usersCollectionUsers.setSourceTypeId(sourceTypeLkp);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldSourceTypeIdOfUsersCollectionUsers != null) {
                    oldSourceTypeIdOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldSourceTypeIdOfUsersCollectionUsers = em.merge(oldSourceTypeIdOfUsersCollectionUsers);
                }
            }
            for (Comments commentsCollectionComments : sourceTypeLkp.getCommentsCollection()) {
                SourceTypeLkp oldSourceTypeIdOfCommentsCollectionComments = commentsCollectionComments.getSourceTypeId();
                commentsCollectionComments.setSourceTypeId(sourceTypeLkp);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldSourceTypeIdOfCommentsCollectionComments != null) {
                    oldSourceTypeIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldSourceTypeIdOfCommentsCollectionComments = em.merge(oldSourceTypeIdOfCommentsCollectionComments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SourceTypeLkp sourceTypeLkp) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SourceTypeLkp persistentSourceTypeLkp = em.find(SourceTypeLkp.class, sourceTypeLkp.getId());
            Collection<Users> usersCollectionOld = persistentSourceTypeLkp.getUsersCollection();
            Collection<Users> usersCollectionNew = sourceTypeLkp.getUsersCollection();
            Collection<Comments> commentsCollectionOld = persistentSourceTypeLkp.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = sourceTypeLkp.getCommentsCollection();
            List<String> illegalOrphanMessages = null;
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comments " + commentsCollectionOldComments + " since its sourceTypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getId());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            sourceTypeLkp.setUsersCollection(usersCollectionNew);
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            sourceTypeLkp.setCommentsCollection(commentsCollectionNew);
            sourceTypeLkp = em.merge(sourceTypeLkp);
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    usersCollectionOldUsers.setSourceTypeId(null);
                    usersCollectionOldUsers = em.merge(usersCollectionOldUsers);
                }
            }
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    SourceTypeLkp oldSourceTypeIdOfUsersCollectionNewUsers = usersCollectionNewUsers.getSourceTypeId();
                    usersCollectionNewUsers.setSourceTypeId(sourceTypeLkp);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldSourceTypeIdOfUsersCollectionNewUsers != null && !oldSourceTypeIdOfUsersCollectionNewUsers.equals(sourceTypeLkp)) {
                        oldSourceTypeIdOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldSourceTypeIdOfUsersCollectionNewUsers = em.merge(oldSourceTypeIdOfUsersCollectionNewUsers);
                    }
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    SourceTypeLkp oldSourceTypeIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getSourceTypeId();
                    commentsCollectionNewComments.setSourceTypeId(sourceTypeLkp);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldSourceTypeIdOfCommentsCollectionNewComments != null && !oldSourceTypeIdOfCommentsCollectionNewComments.equals(sourceTypeLkp)) {
                        oldSourceTypeIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldSourceTypeIdOfCommentsCollectionNewComments = em.merge(oldSourceTypeIdOfCommentsCollectionNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = sourceTypeLkp.getId();
                if (findSourceTypeLkp(id) == null) {
                    throw new NonexistentEntityException("The sourceTypeLkp with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SourceTypeLkp sourceTypeLkp;
            try {
                sourceTypeLkp = em.getReference(SourceTypeLkp.class, id);
                sourceTypeLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sourceTypeLkp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Comments> commentsCollectionOrphanCheck = sourceTypeLkp.getCommentsCollection();
            for (Comments commentsCollectionOrphanCheckComments : commentsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SourceTypeLkp (" + sourceTypeLkp + ") cannot be destroyed since the Comments " + commentsCollectionOrphanCheckComments + " in its commentsCollection field has a non-nullable sourceTypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Users> usersCollection = sourceTypeLkp.getUsersCollection();
            for (Users usersCollectionUsers : usersCollection) {
                usersCollectionUsers.setSourceTypeId(null);
                usersCollectionUsers = em.merge(usersCollectionUsers);
            }
            em.remove(sourceTypeLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SourceTypeLkp> findSourceTypeLkpEntities() {
        return findSourceTypeLkpEntities(true, -1, -1);
    }

    public List<SourceTypeLkp> findSourceTypeLkpEntities(int maxResults, int firstResult) {
        return findSourceTypeLkpEntities(false, maxResults, firstResult);
    }

    private List<SourceTypeLkp> findSourceTypeLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SourceTypeLkp.class));
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

    public SourceTypeLkp findSourceTypeLkp(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SourceTypeLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getSourceTypeLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SourceTypeLkp> rt = cq.from(SourceTypeLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
