/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.logging;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.scify.democracit.demoutils.DataAccess.DBUtils.JSONMessage;
import org.scify.democracit.demoutils.DataAccess.DefaultLoggingDBA;
import org.scify.democracit.demoutils.DataAccess.ILoggingDBA;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class DBAEventLogger extends BaseEventLogger {

    private final ILoggingDBA dbalogger;

    public DBAEventLogger(DataSource dataSource) {
        this.dbalogger = new DefaultLoggingDBA(dataSource);
    }

    @Override
    public void error(long activity_id, Exception e) {
        log.log(Level.SEVERE, e.toString(), e);
        int errorID = getID(STATUS_ERROR);
        dbalogger.finalizedActivity(activity_id, errorID, new JSONMessage(STATUS_ERROR, e.toString()).toJSON());
    }

    @Override
    public void error(long activity_id, String message, Exception e) {
        log.log(Level.SEVERE, message + " : " + e.toString(), e);
        int errorID = getID(STATUS_ERROR);
        dbalogger.finalizedActivity(activity_id, errorID, new JSONMessage(message, e.toString()).toJSON());
    }

    @Override
    public void success(long activity_id, String message) {
        log.log(Level.INFO, message);
        int sucID = getID(STATUS_SUCCESS);
        dbalogger.finalizedActivity(activity_id, sucID, new JSONMessage(message).toJSON());
    }

    @Override
    public long registerActivity(int consultation_id, String moduleName, String JSONMessage) {
        log.log(Level.INFO, "Started ".concat(moduleName).concat(" on {0} for {1} ID: {2}"),
                new Object[]{new Date(), CONSULTATION, consultation_id});
        // acquire module_id, action_id, status_lkp_id FROM DB
        int iModuleID = dbalogger.acquireModuleID(moduleName);
        int iStatusID = dbalogger.acquireStatusID(STATUS_PENDING);
        // register the new activity and return the activity ID
        return dbalogger.injectActivity(iModuleID, iStatusID, JSONMessage);
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName) {
        super.success(activity_id, String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
        int iStatusID = dbalogger.acquireStatusID(STATUS_SUCCESS);
        dbalogger.finalizedActivity(activity_id, iStatusID, null);
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName, String JSONmsg) {
        super.success(activity_id, String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
        int iStatusID = dbalogger.acquireStatusID(STATUS_SUCCESS);
        dbalogger.finalizedActivity(activity_id, iStatusID, JSONmsg);
    }

    private int getID(String status_message) {
        // get error ID from DB
        int errorID = 0;
        Map<Integer, String> statusMSGs = dbalogger.getAvailableStatuses();
        for (Map.Entry<Integer, String> entry : statusMSGs.entrySet()) {
            Integer key = entry.getKey();
            String msg = entry.getValue();
            if (msg.contains(status_message)) {
                errorID = key;
            }
        }
        return errorID;
    }
}
