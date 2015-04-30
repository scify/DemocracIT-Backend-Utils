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
import org.scify.democracit.dao.model.StatusLkp;
import org.scify.democracit.dao.model.ActivityStepTypeLkp;
import org.scify.democracit.dao.model.Activities;
import org.scify.democracit.dao.model.ActivitySteps;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ActivityStepsJpaController implements Serializable {

    public ActivityStepsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivitySteps activitySteps) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatusLkp statusId = activitySteps.getStatusId();
            if (statusId != null) {
                statusId = em.getReference(statusId.getClass(), statusId.getId());
                activitySteps.setStatusId(statusId);
            }
            ActivityStepTypeLkp typeId = activitySteps.getTypeId();
            if (typeId != null) {
                typeId = em.getReference(typeId.getClass(), typeId.getId());
                activitySteps.setTypeId(typeId);
            }
            Activities activityId = activitySteps.getActivityId();
            if (activityId != null) {
                activityId = em.getReference(activityId.getClass(), activityId.getId());
                activitySteps.setActivityId(activityId);
            }
            em.persist(activitySteps);
            if (statusId != null) {
                statusId.getActivityStepsCollection().add(activitySteps);
                statusId = em.merge(statusId);
            }
            if (typeId != null) {
                typeId.getActivityStepsCollection().add(activitySteps);
                typeId = em.merge(typeId);
            }
            if (activityId != null) {
                activityId.getActivityStepsCollection().add(activitySteps);
                activityId = em.merge(activityId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivitySteps activitySteps) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ActivitySteps persistentActivitySteps = em.find(ActivitySteps.class, activitySteps.getId());
            StatusLkp statusIdOld = persistentActivitySteps.getStatusId();
            StatusLkp statusIdNew = activitySteps.getStatusId();
            ActivityStepTypeLkp typeIdOld = persistentActivitySteps.getTypeId();
            ActivityStepTypeLkp typeIdNew = activitySteps.getTypeId();
            Activities activityIdOld = persistentActivitySteps.getActivityId();
            Activities activityIdNew = activitySteps.getActivityId();
            if (statusIdNew != null) {
                statusIdNew = em.getReference(statusIdNew.getClass(), statusIdNew.getId());
                activitySteps.setStatusId(statusIdNew);
            }
            if (typeIdNew != null) {
                typeIdNew = em.getReference(typeIdNew.getClass(), typeIdNew.getId());
                activitySteps.setTypeId(typeIdNew);
            }
            if (activityIdNew != null) {
                activityIdNew = em.getReference(activityIdNew.getClass(), activityIdNew.getId());
                activitySteps.setActivityId(activityIdNew);
            }
            activitySteps = em.merge(activitySteps);
            if (statusIdOld != null && !statusIdOld.equals(statusIdNew)) {
                statusIdOld.getActivityStepsCollection().remove(activitySteps);
                statusIdOld = em.merge(statusIdOld);
            }
            if (statusIdNew != null && !statusIdNew.equals(statusIdOld)) {
                statusIdNew.getActivityStepsCollection().add(activitySteps);
                statusIdNew = em.merge(statusIdNew);
            }
            if (typeIdOld != null && !typeIdOld.equals(typeIdNew)) {
                typeIdOld.getActivityStepsCollection().remove(activitySteps);
                typeIdOld = em.merge(typeIdOld);
            }
            if (typeIdNew != null && !typeIdNew.equals(typeIdOld)) {
                typeIdNew.getActivityStepsCollection().add(activitySteps);
                typeIdNew = em.merge(typeIdNew);
            }
            if (activityIdOld != null && !activityIdOld.equals(activityIdNew)) {
                activityIdOld.getActivityStepsCollection().remove(activitySteps);
                activityIdOld = em.merge(activityIdOld);
            }
            if (activityIdNew != null && !activityIdNew.equals(activityIdOld)) {
                activityIdNew.getActivityStepsCollection().add(activitySteps);
                activityIdNew = em.merge(activityIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = activitySteps.getId();
                if (findActivitySteps(id) == null) {
                    throw new NonexistentEntityException("The activitySteps with id " + id + " no longer exists.");
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
            ActivitySteps activitySteps;
            try {
                activitySteps = em.getReference(ActivitySteps.class, id);
                activitySteps.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activitySteps with id " + id + " no longer exists.", enfe);
            }
            StatusLkp statusId = activitySteps.getStatusId();
            if (statusId != null) {
                statusId.getActivityStepsCollection().remove(activitySteps);
                statusId = em.merge(statusId);
            }
            ActivityStepTypeLkp typeId = activitySteps.getTypeId();
            if (typeId != null) {
                typeId.getActivityStepsCollection().remove(activitySteps);
                typeId = em.merge(typeId);
            }
            Activities activityId = activitySteps.getActivityId();
            if (activityId != null) {
                activityId.getActivityStepsCollection().remove(activitySteps);
                activityId = em.merge(activityId);
            }
            em.remove(activitySteps);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivitySteps> findActivityStepsEntities() {
        return findActivityStepsEntities(true, -1, -1);
    }

    public List<ActivitySteps> findActivityStepsEntities(int maxResults, int firstResult) {
        return findActivityStepsEntities(false, maxResults, firstResult);
    }

    private List<ActivitySteps> findActivityStepsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivitySteps.class));
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

    public ActivitySteps findActivitySteps(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivitySteps.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityStepsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivitySteps> rt = cq.from(ActivitySteps.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
