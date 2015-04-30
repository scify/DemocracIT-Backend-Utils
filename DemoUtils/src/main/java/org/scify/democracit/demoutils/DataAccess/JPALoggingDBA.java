/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.postgresql.util.PGobject;
import org.scify.democracit.dao.jpacontroller.ActivitiesJpaController;
import org.scify.democracit.dao.jpacontroller.ModuleLkpJpaController;
import org.scify.democracit.dao.jpacontroller.StatusLkpJpaController;
import org.scify.democracit.dao.model.ModuleLkp;
import org.scify.democracit.dao.model.StatusLkp;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLUtils;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class JPALoggingDBA implements ILoggingDBA {

    private static JPALoggingDBA loggingDBA;

    private EntityManagerFactory emf;
    private ActivitiesJpaController activitiesController;
    private ModuleLkpJpaController moduleLkpController;
    private StatusLkpJpaController statusLkpController;

    public synchronized static JPALoggingDBA getInstance(EntityManagerFactory emf) {
        if (loggingDBA == null) {
            loggingDBA = new JPALoggingDBA(emf);
        }
        return loggingDBA;
    }

    private JPALoggingDBA(EntityManagerFactory emf) {
        this.activitiesController = new ActivitiesJpaController(emf);
        this.moduleLkpController = new ModuleLkpJpaController(emf);
        this.statusLkpController = new StatusLkpJpaController(emf);
        this.emf = emf;
    }

    @Override
    public int acquireModuleID(String moduleName) {
        ModuleLkp module = moduleLkpController.findModuleLkpByName(moduleName);
        return module.getId();
    }

    @Override
    public int acquireStatusID(String statusName) {
        StatusLkp status = statusLkpController.findStatusByName(statusName);
        return status.getId();
    }

    @Override
    public Map<Integer, String> getAvailableStatuses() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public long injectActivity(int module_id, int status_lkp_id, String JSONmsg) {
        EntityManager em = null;
        CallableStatement callableStatement = null;
        Connection connection = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            connection = em.unwrap(Connection.class);

            callableStatement = connection.prepareCall("{call log.activity_create(?,?,?,?)}");

            callableStatement.setShort(1, (short) module_id);
            callableStatement.setShort(2, (short) status_lkp_id);
            PGobject pgo = new PGobject();
            pgo.setType("json");
            pgo.setValue(JSONmsg);
            callableStatement.setObject(3, pgo);
            callableStatement.registerOutParameter(4, Types.BIGINT);
            callableStatement.execute();

            Long id = callableStatement.getLong(4);
            // commit
            em.getTransaction().commit();

            return id;
        } catch (SQLException ex) {
            Logger.getLogger(JPALoggingDBA.class.getName()).log(Level.SEVERE, null, ex);
            return -1l;
        } finally {
            if (em != null) {
                em.close();
            }
            SQLUtils.release(connection, callableStatement, null);
        }
    }

    @Override
    public void finalizedActivity(long activity_id, int status_lkp_id, String JSONmsg) {
        EntityManager em = null;
        Connection connection = null;
        CallableStatement callableStatement = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            connection = em.unwrap(Connection.class);

            callableStatement = connection.prepareCall("{call log.activity_commit(?,?,?)}");

            callableStatement.setLong(1, activity_id);
            callableStatement.setShort(2, (short) status_lkp_id);
            PGobject pgo = new PGobject();
            pgo.setType("json");
            pgo.setValue(JSONmsg);
            callableStatement.setObject(3, pgo);
            callableStatement.execute();
            // commit
            em.getTransaction().commit();
        } catch (SQLException ex) {
            Logger.getLogger(JPALoggingDBA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (em != null) {
                em.close();
            }
            SQLUtils.release(connection, callableStatement, null);
        }
    }

}
