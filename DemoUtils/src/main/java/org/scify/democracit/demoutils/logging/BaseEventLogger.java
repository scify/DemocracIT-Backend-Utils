/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.logging;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class BaseEventLogger implements ILogger {

    protected DataSource dataSource;
    protected static final Logger log = Logger.getLogger(DBAEventLogger.class.getName());
    public static final String CONSULTATION = "consultation";
    public static final String ARTICLE = "article";

    public static final String STATUS_ERROR = "Error";
    public static final String STATUS_SUCCESS = "Success";
    public static final String STATUS_PENDING = "Pending";

    @Override
    public void success(long activity_id, String message) {
        log.log(Level.INFO, message);
    }

    @Override
    public void warn(String message) {
        log.log(Level.WARNING, message);
    }

    @Override
    public void warn(Exception ex) {
        log.log(Level.WARNING, ex.toString());
    }

    @Override
    public void info(String message) {
        log.log(Level.INFO, message);
    }

    @Override
    public void error(long activity_id, Exception e) {
        log.log(Level.SEVERE, e.toString(), e);
    }

    @Override
    public void error(long activity_id, String message, Exception e) {
        log.log(Level.SEVERE, message + " : " + e.toString(), e);
    }

    @Override
    public long registerActivity(int consultation_id, String moduleName, String JSONMessage) {
        log.log(Level.INFO, "Started ".concat(moduleName).concat(" on {0} for {1} ID: {2}"),
                new Object[]{new Date(), CONSULTATION, consultation_id});
        return -1l;
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName) {
        success(activity_id, String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
    }

    @Override
    public void finalizedActivity(long activity_id, int consultation_id, String moduleName, String JSONmsg) {
        success(activity_id, String.format("Finalized ".concat(moduleName).concat(" on %s for %s ID: %d%n"),
                new Date(), CONSULTATION, consultation_id));
    }

}
