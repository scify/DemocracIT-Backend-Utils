/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.logging;

import java.util.Date;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import org.scify.democracit.dao.jpacontroller.ActivitiesJpaController;
import org.scify.democracit.dao.jpacontroller.ModuleLkpJpaController;
import org.scify.democracit.dao.jpacontroller.StatusLkpJpaController;
import org.scify.democracit.demoutils.DataAccess.DBUtils.JSONMessage;
import org.scify.democracit.demoutils.DataAccess.JPALoggingDBA;
import static org.scify.democracit.demoutils.logging.ILogger.CONSULTATION;
import static org.scify.democracit.demoutils.logging.ILogger.STATUS_SUCCESS;
import static org.scify.democracit.demoutils.logging.ILogger.log;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public class DBAJPAEventLogger implements ILogger {

    private static DBAJPAEventLogger logger;

    private ActivitiesJpaController activitiesController;
    private ModuleLkpJpaController moduleLkpController;
    private StatusLkpJpaController statusLkpController;

    private JPALoggingDBA dbalogging;

    public synchronized static DBAJPAEventLogger getInstance(EntityManagerFactory emf) {
        if (logger == null) {
            logger = new DBAJPAEventLogger(emf);
        }
        return logger;
    }

    private DBAJPAEventLogger(EntityManagerFactory emf) {
        this.activitiesController = new ActivitiesJpaController(emf);
        this.moduleLkpController = new ModuleLkpJpaController(emf);
        this.statusLkpController = new StatusLkpJpaController(emf);
        this.dbalogging = JPALoggingDBA.getInstance(emf);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warning(message);
    }

    @Override
    public void warn(Exception ex) {
        log.log(Level.WARNING, "", ex);
    }

    @Override
    public void error(long activity_id, Exception e) {
        log.log(Level.SEVERE, e.toString(), e);
        int errorID = dbalogging.acquireStatusID(STATUS_ERROR);
        dbalogging.finalizedActivity(activity_id, errorID, new JSONMessage(STATUS_ERROR, e.toString()).toJSON());
    }

    @Override
    public void error(long activity_id, String message, Exception e) {
        log.log(Level.SEVERE, message + " : " + e.toString(), e);
        int errorID = dbalogging.acquireStatusID(STATUS_ERROR);
        dbalogging.finalizedActivity(activity_id, errorID, new JSONMessage(message, e.toString()).toJSON());
    }

    @Override
    public void success(long activity_id, String message) {
        log.log(Level.INFO, message);
        int sucID = statusLkpController.findStatusByName(STATUS_SUCCESS).getId();
        dbalogging.finalizedActivity(activity_id, sucID, new JSONMessage(message).toJSON());
    }

    @Override
    public long registerActivity(int consultation_id, String moduleName, String JSONMessage) {
        log.log(Level.INFO, "Started ".concat(moduleName).concat(" on {0} for {1} ID: {2}"),
                new Object[]{new Date(), CONSULTATION, consultation_id});
        // acquire module_id, action_id, status_lkp_id FROM DB
        int iModuleID = dbalogging.acquireModuleID(moduleName);
        int iStatusID = dbalogging.acquireStatusID(STATUS_PENDING);
        // register the new activity and return the activity ID
        return dbalogging.injectActivity(iModuleID, iStatusID, JSONMessage);
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName) {
        log.info(String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
        int iStatusID = dbalogging.acquireStatusID(STATUS_SUCCESS);
        dbalogging.finalizedActivity(activity_id, iStatusID, null);
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName, String JSONmsg) {
        log.info(String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
        int iStatusID = dbalogging.acquireStatusID(STATUS_SUCCESS);
        dbalogging.finalizedActivity(activity_id, iStatusID, JSONmsg);
    }

}
