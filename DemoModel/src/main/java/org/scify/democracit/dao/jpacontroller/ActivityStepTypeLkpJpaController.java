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
import org.scify.democracit.dao.model.ActivitySteps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.jpacontroller.exceptions.PreexistingEntityException;
import org.scify.democracit.dao.model.ActivityStepTypeLkp;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ActivityStepTypeLkpJpaController implements Serializable {

    public ActivityStepTypeLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivityStepTypeLkp activityStepTypeLkp) throws PreexistingEntityException, Exception {
        if (activityStepTypeLkp.getActivityStepsCollection() == null) {
            activityStepTypeLkp.setActivityStepsCollection(new ArrayList<ActivitySteps>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ActivitySteps> attachedActivityStepsCollection = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionActivityStepsToAttach : activityStepTypeLkp.getActivityStepsCollection()) {
                activityStepsCollectionActivityStepsToAttach = em.getReference(activityStepsCollectionActivityStepsToAttach.getClass(), activityStepsCollectionActivityStepsToAttach.getId());
                attachedActivityStepsCollection.add(activityStepsCollectionActivityStepsToAttach);
            }
            activityStepTypeLkp.setActivityStepsCollection(attachedActivityStepsCollection);
            em.persist(activityStepTypeLkp);
            for (ActivitySteps activityStepsCollectionActivitySteps : activityStepTypeLkp.getActivityStepsCollection()) {
                ActivityStepTypeLkp oldTypeIdOfActivityStepsCollectionActivitySteps = activityStepsCollectionActivitySteps.getTypeId();
                activityStepsCollectionActivitySteps.setTypeId(activityStepTypeLkp);
                activityStepsCollectionActivitySteps = em.merge(activityStepsCollectionActivitySteps);
                if (oldTypeIdOfActivityStepsCollectionActivitySteps != null) {
                    oldTypeIdOfActivityStepsCollectionActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionActivitySteps);
                    oldTypeIdOfActivityStepsCollectionActivitySteps = em.merge(oldTypeIdOfActivityStepsCollectionActivitySteps);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findActivityStepTypeLkp(activityStepTypeLkp.getId()) != null) {
                throw new PreexistingEntityException("ActivityStepTypeLkp " + activityStepTypeLkp + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivityStepTypeLkp activityStepTypeLkp) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivityStepTypeLkp persistentActivityStepTypeLkp = em.find(ActivityStepTypeLkp.class, activityStepTypeLkp.getId());
            Collection<ActivitySteps> activityStepsCollectionOld = persistentActivityStepTypeLkp.getActivityStepsCollection();
            Collection<ActivitySteps> activityStepsCollectionNew = activityStepTypeLkp.getActivityStepsCollection();
            List<String> illegalOrphanMessages = null;
            for (ActivitySteps activityStepsCollectionOldActivitySteps : activityStepsCollectionOld) {
                if (!activityStepsCollectionNew.contains(activityStepsCollectionOldActivitySteps)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ActivitySteps " + activityStepsCollectionOldActivitySteps + " since its typeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ActivitySteps> attachedActivityStepsCollectionNew = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionNewActivityStepsToAttach : activityStepsCollectionNew) {
                activityStepsCollectionNewActivityStepsToAttach = em.getReference(activityStepsCollectionNewActivityStepsToAttach.getClass(), activityStepsCollectionNewActivityStepsToAttach.getId());
                attachedActivityStepsCollectionNew.add(activityStepsCollectionNewActivityStepsToAttach);
            }
            activityStepsCollectionNew = attachedActivityStepsCollectionNew;
            activityStepTypeLkp.setActivityStepsCollection(activityStepsCollectionNew);
            activityStepTypeLkp = em.merge(activityStepTypeLkp);
            for (ActivitySteps activityStepsCollectionNewActivitySteps : activityStepsCollectionNew) {
                if (!activityStepsCollectionOld.contains(activityStepsCollectionNewActivitySteps)) {
                    ActivityStepTypeLkp oldTypeIdOfActivityStepsCollectionNewActivitySteps = activityStepsCollectionNewActivitySteps.getTypeId();
                    activityStepsCollectionNewActivitySteps.setTypeId(activityStepTypeLkp);
                    activityStepsCollectionNewActivitySteps = em.merge(activityStepsCollectionNewActivitySteps);
                    if (oldTypeIdOfActivityStepsCollectionNewActivitySteps != null && !oldTypeIdOfActivityStepsCollectionNewActivitySteps.equals(activityStepTypeLkp)) {
                        oldTypeIdOfActivityStepsCollectionNewActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionNewActivitySteps);
                        oldTypeIdOfActivityStepsCollectionNewActivitySteps = em.merge(oldTypeIdOfActivityStepsCollectionNewActivitySteps);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = activityStepTypeLkp.getId();
                if (findActivityStepTypeLkp(id) == null) {
                    throw new NonexistentEntityException("The activityStepTypeLkp with id " + id + " no longer exists.");
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
            ActivityStepTypeLkp activityStepTypeLkp;
            try {
                activityStepTypeLkp = em.getReference(ActivityStepTypeLkp.class, id);
                activityStepTypeLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activityStepTypeLkp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ActivitySteps> activityStepsCollectionOrphanCheck = activityStepTypeLkp.getActivityStepsCollection();
            for (ActivitySteps activityStepsCollectionOrphanCheckActivitySteps : activityStepsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ActivityStepTypeLkp (" + activityStepTypeLkp + ") cannot be destroyed since the ActivitySteps " + activityStepsCollectionOrphanCheckActivitySteps + " in its activityStepsCollection field has a non-nullable typeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(activityStepTypeLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivityStepTypeLkp> findActivityStepTypeLkpEntities() {
        return findActivityStepTypeLkpEntities(true, -1, -1);
    }

    public List<ActivityStepTypeLkp> findActivityStepTypeLkpEntities(int maxResults, int firstResult) {
        return findActivityStepTypeLkpEntities(false, maxResults, firstResult);
    }

    private List<ActivityStepTypeLkp> findActivityStepTypeLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivityStepTypeLkp.class));
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

    public ActivityStepTypeLkp findActivityStepTypeLkp(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivityStepTypeLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityStepTypeLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivityStepTypeLkp> rt = cq.from(ActivityStepTypeLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
