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
import org.scify.democracit.dao.model.Activities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.scify.democracit.dao.jpacontroller.exceptions.IllegalOrphanException;
import org.scify.democracit.dao.jpacontroller.exceptions.NonexistentEntityException;
import org.scify.democracit.dao.model.ActivitySteps;
import org.scify.democracit.dao.model.StatusLkp;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class StatusLkpJpaController implements Serializable {

    public StatusLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StatusLkp statusLkp) {
        if (statusLkp.getActivitiesCollection() == null) {
            statusLkp.setActivitiesCollection(new ArrayList<Activities>());
        }
        if (statusLkp.getActivityStepsCollection() == null) {
            statusLkp.setActivityStepsCollection(new ArrayList<ActivitySteps>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Activities> attachedActivitiesCollection = new ArrayList<Activities>();
            for (Activities activitiesCollectionActivitiesToAttach : statusLkp.getActivitiesCollection()) {
                activitiesCollectionActivitiesToAttach = em.getReference(activitiesCollectionActivitiesToAttach.getClass(), activitiesCollectionActivitiesToAttach.getId());
                attachedActivitiesCollection.add(activitiesCollectionActivitiesToAttach);
            }
            statusLkp.setActivitiesCollection(attachedActivitiesCollection);
            Collection<ActivitySteps> attachedActivityStepsCollection = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionActivityStepsToAttach : statusLkp.getActivityStepsCollection()) {
                activityStepsCollectionActivityStepsToAttach = em.getReference(activityStepsCollectionActivityStepsToAttach.getClass(), activityStepsCollectionActivityStepsToAttach.getId());
                attachedActivityStepsCollection.add(activityStepsCollectionActivityStepsToAttach);
            }
            statusLkp.setActivityStepsCollection(attachedActivityStepsCollection);
            em.persist(statusLkp);
            for (Activities activitiesCollectionActivities : statusLkp.getActivitiesCollection()) {
                StatusLkp oldStatusIdOfActivitiesCollectionActivities = activitiesCollectionActivities.getStatusId();
                activitiesCollectionActivities.setStatusId(statusLkp);
                activitiesCollectionActivities = em.merge(activitiesCollectionActivities);
                if (oldStatusIdOfActivitiesCollectionActivities != null) {
                    oldStatusIdOfActivitiesCollectionActivities.getActivitiesCollection().remove(activitiesCollectionActivities);
                    oldStatusIdOfActivitiesCollectionActivities = em.merge(oldStatusIdOfActivitiesCollectionActivities);
                }
            }
            for (ActivitySteps activityStepsCollectionActivitySteps : statusLkp.getActivityStepsCollection()) {
                StatusLkp oldStatusIdOfActivityStepsCollectionActivitySteps = activityStepsCollectionActivitySteps.getStatusId();
                activityStepsCollectionActivitySteps.setStatusId(statusLkp);
                activityStepsCollectionActivitySteps = em.merge(activityStepsCollectionActivitySteps);
                if (oldStatusIdOfActivityStepsCollectionActivitySteps != null) {
                    oldStatusIdOfActivityStepsCollectionActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionActivitySteps);
                    oldStatusIdOfActivityStepsCollectionActivitySteps = em.merge(oldStatusIdOfActivityStepsCollectionActivitySteps);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StatusLkp statusLkp) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusLkp persistentStatusLkp = em.find(StatusLkp.class, statusLkp.getId());
            Collection<Activities> activitiesCollectionOld = persistentStatusLkp.getActivitiesCollection();
            Collection<Activities> activitiesCollectionNew = statusLkp.getActivitiesCollection();
            Collection<ActivitySteps> activityStepsCollectionOld = persistentStatusLkp.getActivityStepsCollection();
            Collection<ActivitySteps> activityStepsCollectionNew = statusLkp.getActivityStepsCollection();
            List<String> illegalOrphanMessages = null;
            for (Activities activitiesCollectionOldActivities : activitiesCollectionOld) {
                if (!activitiesCollectionNew.contains(activitiesCollectionOldActivities)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Activities " + activitiesCollectionOldActivities + " since its statusId field is not nullable.");
                }
            }
            for (ActivitySteps activityStepsCollectionOldActivitySteps : activityStepsCollectionOld) {
                if (!activityStepsCollectionNew.contains(activityStepsCollectionOldActivitySteps)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ActivitySteps " + activityStepsCollectionOldActivitySteps + " since its statusId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Activities> attachedActivitiesCollectionNew = new ArrayList<Activities>();
            for (Activities activitiesCollectionNewActivitiesToAttach : activitiesCollectionNew) {
                activitiesCollectionNewActivitiesToAttach = em.getReference(activitiesCollectionNewActivitiesToAttach.getClass(), activitiesCollectionNewActivitiesToAttach.getId());
                attachedActivitiesCollectionNew.add(activitiesCollectionNewActivitiesToAttach);
            }
            activitiesCollectionNew = attachedActivitiesCollectionNew;
            statusLkp.setActivitiesCollection(activitiesCollectionNew);
            Collection<ActivitySteps> attachedActivityStepsCollectionNew = new ArrayList<ActivitySteps>();
            for (ActivitySteps activityStepsCollectionNewActivityStepsToAttach : activityStepsCollectionNew) {
                activityStepsCollectionNewActivityStepsToAttach = em.getReference(activityStepsCollectionNewActivityStepsToAttach.getClass(), activityStepsCollectionNewActivityStepsToAttach.getId());
                attachedActivityStepsCollectionNew.add(activityStepsCollectionNewActivityStepsToAttach);
            }
            activityStepsCollectionNew = attachedActivityStepsCollectionNew;
            statusLkp.setActivityStepsCollection(activityStepsCollectionNew);
            statusLkp = em.merge(statusLkp);
            for (Activities activitiesCollectionNewActivities : activitiesCollectionNew) {
                if (!activitiesCollectionOld.contains(activitiesCollectionNewActivities)) {
                    StatusLkp oldStatusIdOfActivitiesCollectionNewActivities = activitiesCollectionNewActivities.getStatusId();
                    activitiesCollectionNewActivities.setStatusId(statusLkp);
                    activitiesCollectionNewActivities = em.merge(activitiesCollectionNewActivities);
                    if (oldStatusIdOfActivitiesCollectionNewActivities != null && !oldStatusIdOfActivitiesCollectionNewActivities.equals(statusLkp)) {
                        oldStatusIdOfActivitiesCollectionNewActivities.getActivitiesCollection().remove(activitiesCollectionNewActivities);
                        oldStatusIdOfActivitiesCollectionNewActivities = em.merge(oldStatusIdOfActivitiesCollectionNewActivities);
                    }
                }
            }
            for (ActivitySteps activityStepsCollectionNewActivitySteps : activityStepsCollectionNew) {
                if (!activityStepsCollectionOld.contains(activityStepsCollectionNewActivitySteps)) {
                    StatusLkp oldStatusIdOfActivityStepsCollectionNewActivitySteps = activityStepsCollectionNewActivitySteps.getStatusId();
                    activityStepsCollectionNewActivitySteps.setStatusId(statusLkp);
                    activityStepsCollectionNewActivitySteps = em.merge(activityStepsCollectionNewActivitySteps);
                    if (oldStatusIdOfActivityStepsCollectionNewActivitySteps != null && !oldStatusIdOfActivityStepsCollectionNewActivitySteps.equals(statusLkp)) {
                        oldStatusIdOfActivityStepsCollectionNewActivitySteps.getActivityStepsCollection().remove(activityStepsCollectionNewActivitySteps);
                        oldStatusIdOfActivityStepsCollectionNewActivitySteps = em.merge(oldStatusIdOfActivityStepsCollectionNewActivitySteps);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = statusLkp.getId();
                if (findStatusLkp(id) == null) {
                    throw new NonexistentEntityException("The statusLkp with id " + id + " no longer exists.");
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
            StatusLkp statusLkp;
            try {
                statusLkp = em.getReference(StatusLkp.class, id);
                statusLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statusLkp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Activities> activitiesCollectionOrphanCheck = statusLkp.getActivitiesCollection();
            for (Activities activitiesCollectionOrphanCheckActivities : activitiesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StatusLkp (" + statusLkp + ") cannot be destroyed since the Activities " + activitiesCollectionOrphanCheckActivities + " in its activitiesCollection field has a non-nullable statusId field.");
            }
            Collection<ActivitySteps> activityStepsCollectionOrphanCheck = statusLkp.getActivityStepsCollection();
            for (ActivitySteps activityStepsCollectionOrphanCheckActivitySteps : activityStepsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StatusLkp (" + statusLkp + ") cannot be destroyed since the ActivitySteps " + activityStepsCollectionOrphanCheckActivitySteps + " in its activityStepsCollection field has a non-nullable statusId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(statusLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StatusLkp> findStatusLkpEntities() {
        return findStatusLkpEntities(true, -1, -1);
    }

    public List<StatusLkp> findStatusLkpEntities(int maxResults, int firstResult) {
        return findStatusLkpEntities(false, maxResults, firstResult);
    }

    private List<StatusLkp> findStatusLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatusLkp.class));
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

    public StatusLkp findStatusLkp(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatusLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatusLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatusLkp> rt = cq.from(StatusLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public StatusLkp findStatusByName(String status_condition) {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<StatusLkp> query
                    = em.createNamedQuery("StatusLkp.findByStatusDescription", StatusLkp.class);
            query.setParameter("statusDescription", status_condition);
            StatusLkp res = query.getSingleResult();
            return res;
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

}
