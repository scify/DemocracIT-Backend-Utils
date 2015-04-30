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
import org.scify.democracit.dao.model.SourceTypeLkp;
import org.scify.democracit.dao.model.Comments;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.jpacontroller.exceptions.PreexistingEntityException;
import org.scify.democracit.dao.model.Users;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getCommentsCollection() == null) {
            users.setCommentsCollection(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SourceTypeLkp sourceTypeId = users.getSourceTypeId();
            if (sourceTypeId != null) {
                sourceTypeId = em.getReference(sourceTypeId.getClass(), sourceTypeId.getId());
                users.setSourceTypeId(sourceTypeId);
            }
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : users.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            users.setCommentsCollection(attachedCommentsCollection);
            em.persist(users);
            if (sourceTypeId != null) {
                sourceTypeId.getUsersCollection().add(users);
                sourceTypeId = em.merge(sourceTypeId);
            }
            for (Comments commentsCollectionComments : users.getCommentsCollection()) {
                Users oldUserIdOfCommentsCollectionComments = commentsCollectionComments.getUserId();
                commentsCollectionComments.setUserId(users);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldUserIdOfCommentsCollectionComments != null) {
                    oldUserIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldUserIdOfCommentsCollectionComments = em.merge(oldUserIdOfCommentsCollectionComments);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getId()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            SourceTypeLkp sourceTypeIdOld = persistentUsers.getSourceTypeId();
            SourceTypeLkp sourceTypeIdNew = users.getSourceTypeId();
            Collection<Comments> commentsCollectionOld = persistentUsers.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = users.getCommentsCollection();
            if (sourceTypeIdNew != null) {
                sourceTypeIdNew = em.getReference(sourceTypeIdNew.getClass(), sourceTypeIdNew.getId());
                users.setSourceTypeId(sourceTypeIdNew);
            }
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            users.setCommentsCollection(commentsCollectionNew);
            users = em.merge(users);
            if (sourceTypeIdOld != null && !sourceTypeIdOld.equals(sourceTypeIdNew)) {
                sourceTypeIdOld.getUsersCollection().remove(users);
                sourceTypeIdOld = em.merge(sourceTypeIdOld);
            }
            if (sourceTypeIdNew != null && !sourceTypeIdNew.equals(sourceTypeIdOld)) {
                sourceTypeIdNew.getUsersCollection().add(users);
                sourceTypeIdNew = em.merge(sourceTypeIdNew);
            }
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    commentsCollectionOldComments.setUserId(null);
                    commentsCollectionOldComments = em.merge(commentsCollectionOldComments);
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Users oldUserIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getUserId();
                    commentsCollectionNewComments.setUserId(users);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldUserIdOfCommentsCollectionNewComments != null && !oldUserIdOfCommentsCollectionNewComments.equals(users)) {
                        oldUserIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldUserIdOfCommentsCollectionNewComments = em.merge(oldUserIdOfCommentsCollectionNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            SourceTypeLkp sourceTypeId = users.getSourceTypeId();
            if (sourceTypeId != null) {
                sourceTypeId.getUsersCollection().remove(users);
                sourceTypeId = em.merge(sourceTypeId);
            }
            Collection<Comments> commentsCollection = users.getCommentsCollection();
            for (Comments commentsCollectionComments : commentsCollection) {
                commentsCollectionComments.setUserId(null);
                commentsCollectionComments = em.merge(commentsCollectionComments);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
