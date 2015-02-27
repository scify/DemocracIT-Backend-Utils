/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess;

import java.util.Map;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public interface ILoggingDBA {

    /**
     *
     * @param actionName the action name, i.e. 'crawling'
     * @return the ID of the action in the schema
     */
    int acquireActionID(String actionName);

    /**
     *
     * @param moduleName the module name, i.e. 'crawler'
     * @return the id of the module in the schema
     */
    int acquireModuleID(String moduleName);

    /**
     *
     * @param statusName the status name (e.g. 'pending')
     * @return the id of the message in the schema
     */
    int acquireStatusID(String statusName);

    /**
     *
     * @return the <id, status_message> list from the democracit.status_lkp
     * schema
     */
    Map<Integer, String> getAvailableStatuses();

    /**
     *
     * @param module_id the module_id that starts the activity
     * @param status_lkp_id the status_id of the activity
     * @param JSONmsg a custom JSON message, for clarity reasons
     * @return the current activity_id
     */
    long injectActivity(int module_id, int status_lkp_id, String JSONmsg);

    /**
     *
     * @param activity_id the activity that just finished
     * @param status_lkp_id the status_id of the finished activity (success,
     * error, etc)
     * @param JSONmsg a custom JSON message for clarity reasons
     */
    void finalizedActivity(long activity_id, int status_lkp_id, String JSONmsg);

}
