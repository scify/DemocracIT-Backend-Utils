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
import org.scify.democracit.dao.model.StatusLkp;
import org.scify.democracit.dao.model.ModuleLkp;
import org.scify.democracit.dao.model.ActivitySteps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.Activities;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ActivitiesJpaController implements Serializable {

    public ActivitiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Activities activities) {
        if (activities.getActivityStepsCollection() == null) {
            activities.setActivityStepsCollection(new ArrayList<ActivitySteps>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusLkp statusId = activities.getStatusId();
            if (statusId != null) {
                statusId = em.getReference(statusId.getClass(), statusId.getId());
                activities.setStatusId(statusId);
            }
            ModuleLkp moduleId = activities.getModuleId();
            if (moduleId != null) {
                moduleId = em.getReference(moduleId.getClass(), moduleId.getId());
                activities.setModuleId(moduleId);
            }
            Collection<ActivitySteps> attachedActivityStepsCollection = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionActivityStepsToAttach : activities.getActivityStepsCollection()) {
                activityStepsCollectionActivityStepsToAttach = em.getReference(activityStepsCollectionActivityStepsToAttach.getClass(), activityStepsCollectionActivityStepsToAttach.getId());
                attachedActivityStepsCollection.add(activityStepsCollectionActivityStepsToAttach);
            }
            activities.setActivityStepsCollection(attachedActivityStepsCollection);
            em.persist(activities);
            if (statusId != null) {
                statusId.getActivitiesCollection().add(activities);
                statusId = em.merge(statusId);
            }
            if (moduleId != null) {
                moduleId.getActivitiesCollection().add(activities);
                moduleId = em.merge(moduleId);
            }
            for (ActivitySteps activityStepsCollectionActivitySteps : activities.getActivityStepsCollection()) {
                Activities oldActivityIdOfActivityStepsCollectionActivitySteps = activityStepsCollectionActivitySteps.getActivityId();
                activityStepsCollectionActivitySteps.setActivityId(activities);
                activityStepsCollectionActivitySteps = em.merge(activityStepsCollectionActivitySteps);
                if (oldActivityIdOfActivityStepsCollectionActivitySteps != null) {
                    oldActivityIdOfActivityStepsCollectionActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionActivitySteps);
                    oldActivityIdOfActivityStepsCollectionActivitySteps = em.merge(oldActivityIdOfActivityStepsCollectionActivitySteps);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Activities activities) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activities persistentActivities = em.find(Activities.class, activities.getId());
            StatusLkp statusIdOld = persistentActivities.getStatusId();
            StatusLkp statusIdNew = activities.getStatusId();
            ModuleLkp moduleIdOld = persistentActivities.getModuleId();
            ModuleLkp moduleIdNew = activities.getModuleId();
            Collection<ActivitySteps> activityStepsCollectionOld = persistentActivities.getActivityStepsCollection();
            Collection<ActivitySteps> activityStepsCollectionNew = activities.getActivityStepsCollection();
            List<String> illegalOrphanMessages = null;
            for (ActivitySteps activityStepsCollectionOldActivitySteps : activityStepsCollectionOld) {
                if (!activityStepsCollectionNew.contains(activityStepsCollectionOldActivitySteps)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ActivitySteps " + activityStepsCollectionOldActivitySteps + " since its activityId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (statusIdNew != null) {
                statusIdNew = em.getReference(statusIdNew.getClass(), statusIdNew.getId());
                activities.setStatusId(statusIdNew);
            }
            if (moduleIdNew != null) {
                moduleIdNew = em.getReference(moduleIdNew.getClass(), moduleIdNew.getId());
                activities.setModuleId(moduleIdNew);
            }
            Collection<ActivitySteps> attachedActivityStepsCollectionNew = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionNewActivityStepsToAttach : activityStepsCollectionNew) {
                activityStepsCollectionNewActivityStepsToAttach = em.getReference(activityStepsCollectionNewActivityStepsToAttach.getClass(), activityStepsCollectionNewActivityStepsToAttach.getId());
                attachedActivityStepsCollectionNew.add(activityStepsCollectionNewActivityStepsToAttach);
            }
            activityStepsCollectionNew = attachedActivityStepsCollectionNew;
            activities.setActivityStepsCollection(activityStepsCollectionNew);
            activities = em.merge(activities);
            if (statusIdOld != null && !statusIdOld.equals(statusIdNew)) {
                statusIdOld.getActivitiesCollection().remove(activities);
                statusIdOld = em.merge(statusIdOld);
            }
            if (statusIdNew != null && !statusIdNew.equals(statusIdOld)) {
                statusIdNew.getActivitiesCollection().add(activities);
                statusIdNew = em.merge(statusIdNew);
            }
            if (moduleIdOld != null && !moduleIdOld.equals(moduleIdNew)) {
                moduleIdOld.getActivitiesCollection().remove(activities);
                moduleIdOld = em.merge(moduleIdOld);
            }
            if (moduleIdNew != null && !moduleIdNew.equals(moduleIdOld)) {
                moduleIdNew.getActivitiesCollection().add(activities);
                moduleIdNew = em.merge(moduleIdNew);
            }
            for (ActivitySteps activityStepsCollectionNewActivitySteps : activityStepsCollectionNew) {
                if (!activityStepsCollectionOld.contains(activityStepsCollectionNewActivitySteps)) {
                    Activities oldActivityIdOfActivityStepsCollectionNewActivitySteps = activityStepsCollectionNewActivitySteps.getActivityId();
                    activityStepsCollectionNewActivitySteps.setActivityId(activities);
                    activityStepsCollectionNewActivitySteps = em.merge(activityStepsCollectionNewActivitySteps);
                    if (oldActivityIdOfActivityStepsCollectionNewActivitySteps != null && !oldActivityIdOfActivityStepsCollectionNewActivitySteps.equals(activities)) {
                        oldActivityIdOfActivityStepsCollectionNewActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionNewActivitySteps);
                        oldActivityIdOfActivityStepsCollectionNewActivitySteps = em.merge(oldActivityIdOfActivityStepsCollectionNewActivitySteps);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = activities.getId();
                if (findActivities(id) == null) {
                    throw new NonexistentEntityException("The activities with id " + id + " no longer exists.");
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
            Activities activities;
            try {
                activities = em.getReference(Activities.class, id);
                activities.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activities with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ActivitySteps> activityStepsCollectionOrphanCheck = activities.getActivityStepsCollection();
            for (ActivitySteps activityStepsCollectionOrphanCheckActivitySteps : activityStepsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Activities (" + activities + ") cannot be destroyed since the ActivitySteps " + activityStepsCollectionOrphanCheckActivitySteps + " in its activityStepsCollection field has a non-nullable activityId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            StatusLkp statusId = activities.getStatusId();
            if (statusId != null) {
                statusId.getActivitiesCollection().remove(activities);
                statusId = em.merge(statusId);
            }
            ModuleLkp moduleId = activities.getModuleId();
            if (moduleId != null) {
                moduleId.getActivitiesCollection().remove(activities);
                moduleId = em.merge(moduleId);
            }
            em.remove(activities);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Activities> findActivitiesEntities() {
        return findActivitiesEntities(true, -1, -1);
    }

    public List<Activities> findActivitiesEntities(int maxResults, int firstResult) {
        return findActivitiesEntities(false, maxResults, firstResult);
    }

    private List<Activities> findActivitiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Activities.class));
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

    public Activities findActivities(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Activities.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivitiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Activities> rt = cq.from(Activities.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
