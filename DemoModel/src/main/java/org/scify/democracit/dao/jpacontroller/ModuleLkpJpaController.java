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
import org.scify.democracit.dao.model.ModuleLkp;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class ModuleLkpJpaController implements Serializable {

    public ModuleLkpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ModuleLkp moduleLkp) {
        if (moduleLkp.getActivitiesCollection() == null) {
            moduleLkp.setActivitiesCollection(new ArrayList<Activities>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Activities> attachedActivitiesCollection = new ArrayList<Activities>();
            for (Activities activitiesCollectionActivitiesToAttach : moduleLkp.getActivitiesCollection()) {
                activitiesCollectionActivitiesToAttach = em.getReference(activitiesCollectionActivitiesToAttach.getClass(), activitiesCollectionActivitiesToAttach.getId());
                attachedActivitiesCollection.add(activitiesCollectionActivitiesToAttach);
            }
            moduleLkp.setActivitiesCollection(attachedActivitiesCollection);
            em.persist(moduleLkp);
            for (Activities activitiesCollectionActivities : moduleLkp.getActivitiesCollection()) {
                ModuleLkp oldModuleIdOfActivitiesCollectionActivities = activitiesCollectionActivities.getModuleId();
                activitiesCollectionActivities.setModuleId(moduleLkp);
                activitiesCollectionActivities = em.merge(activitiesCollectionActivities);
                if (oldModuleIdOfActivitiesCollectionActivities != null) {
                    oldModuleIdOfActivitiesCollectionActivities.getActivitiesCollection().remove(activitiesCollectionActivities);
                    oldModuleIdOfActivitiesCollectionActivities = em.merge(oldModuleIdOfActivitiesCollectionActivities);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ModuleLkp moduleLkp) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ModuleLkp persistentModuleLkp = em.find(ModuleLkp.class, moduleLkp.getId());
            Collection<Activities> activitiesCollectionOld = persistentModuleLkp.getActivitiesCollection();
            Collection<Activities> activitiesCollectionNew = moduleLkp.getActivitiesCollection();
            List<String> illegalOrphanMessages = null;
            for (Activities activitiesCollectionOldActivities : activitiesCollectionOld) {
                if (!activitiesCollectionNew.contains(activitiesCollectionOldActivities)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Activities " + activitiesCollectionOldActivities + " since its moduleId field is not nullable.");
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
            moduleLkp.setActivitiesCollection(activitiesCollectionNew);
            moduleLkp = em.merge(moduleLkp);
            for (Activities activitiesCollectionNewActivities : activitiesCollectionNew) {
                if (!activitiesCollectionOld.contains(activitiesCollectionNewActivities)) {
                    ModuleLkp oldModuleIdOfActivitiesCollectionNewActivities = activitiesCollectionNewActivities.getModuleId();
                    activitiesCollectionNewActivities.setModuleId(moduleLkp);
                    activitiesCollectionNewActivities = em.merge(activitiesCollectionNewActivities);
                    if (oldModuleIdOfActivitiesCollectionNewActivities != null && !oldModuleIdOfActivitiesCollectionNewActivities.equals(moduleLkp)) {
                        oldModuleIdOfActivitiesCollectionNewActivities.getActivitiesCollection().remove(activitiesCollectionNewActivities);
                        oldModuleIdOfActivitiesCollectionNewActivities = em.merge(oldModuleIdOfActivitiesCollectionNewActivities);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = moduleLkp.getId();
                if (findModuleLkp(id) == null) {
                    throw new NonexistentEntityException("The moduleLkp with id " + id + " no longer exists.");
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
            ModuleLkp moduleLkp;
            try {
                moduleLkp = em.getReference(ModuleLkp.class, id);
                moduleLkp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moduleLkp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Activities> activitiesCollectionOrphanCheck = moduleLkp.getActivitiesCollection();
            for (Activities activitiesCollectionOrphanCheckActivities : activitiesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ModuleLkp (" + moduleLkp + ") cannot be destroyed since the Activities " + activitiesCollectionOrphanCheckActivities + " in its activitiesCollection field has a non-nullable moduleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(moduleLkp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ModuleLkp> findModuleLkpEntities() {
        return findModuleLkpEntities(true, -1, -1);
    }

    public List<ModuleLkp> findModuleLkpEntities(int maxResults, int firstResult) {
        return findModuleLkpEntities(false, maxResults, firstResult);
    }

    private List<ModuleLkp> findModuleLkpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ModuleLkp.class));
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

    public ModuleLkp findModuleLkp(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ModuleLkp.class, id);
        } finally {
            em.close();
        }
    }

    public int getModuleLkpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ModuleLkp> rt = cq.from(ModuleLkp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ModuleLkp findModuleLkpByName(String actionName) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<ModuleLkp> query
                    = em.createNamedQuery("ModuleLkp.findByName", ModuleLkp.class);
            query.setParameter("name", actionName);
            ModuleLkp result = query.getSingleResult();
            return result;
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

}
