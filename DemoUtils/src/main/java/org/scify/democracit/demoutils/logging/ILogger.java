/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.logging;

/**
 *
 * @author George K.<gkiom@iit.demokritos.gr>
 */
public interface ILogger {

    void info(String message);

    void warn(String message);

    void warn(Exception ex);

    void error(long activity_id, Exception e);

    void error(long activity_id, String message, Exception e);

    void success(long activity_id, String message);

    /**
     * register a new activity in the DB
     *
     * @param consultation_id the consultation ID
     * @param moduleName the module name, as it is already registered in the
     * module_lkp schema
     * @param JSONMessage a custom JSON message
     * @return the ID of the activity
     */
    long registerActivity(int consultation_id, String moduleName, String JSONMessage);

    /**
     *
     * @param activity_id the activity ID that just finalized
     * @param consultation_id the consultation ID that this activity was
     * operating
     * @param moduleName the module that owned the activity
     */
    void finalizedActivity(long activity_id, int consultation_id, String moduleName);

    /**
     *
     * @param activity_id the activity ID that just finalized
     * @param consultation_id the consultation ID that this activity was
     * operating
     * @param moduleName the module that owned the activity
     * @param JSONmsg a custom JSON message
     */
    void finalizedActivity(long activity_id, int consultation_id, String moduleName, String JSONmsg);
}
