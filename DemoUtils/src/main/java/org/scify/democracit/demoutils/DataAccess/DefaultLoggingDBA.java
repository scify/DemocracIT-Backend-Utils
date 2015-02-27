/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.scify.democracit.demoutils.DataAccess.DBUtils.SQLUtils;
import org.scify.democracit.demoutils.DataAccess.ds.AbstractDSAccess;

/**
 *
 * @author George K. <gkiom@scify.org>
 */
public class DefaultLoggingDBA extends AbstractDSAccess implements ILoggingDBA {

    public DefaultLoggingDBA(DataSource datasource) {
        super(datasource);
    }

    @Override
    public int acquireModuleID(String moduleName) {
        int iRes = 0;
        String SQL = "SELECT id FROM log.module_lkp WHERE name=? LIMIT 1;";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            pStmt.setString(1, moduleName);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                iRes = rSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, rSet);
        }
        return iRes;
    }

    @Override
    public int acquireActionID(String actionName) {
        int iRes = 0;
        String SQL = "SELECT id FROM log.action_lkp WHERE name=? LIMIT 1;";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            pStmt.setString(1, actionName);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                iRes = rSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, rSet);
        }
        return iRes;
    }

    @Override
    public int acquireStatusID(String statusName) {
        int iRes = 0;
        String SQL = "SELECT id FROM log.status_lkp WHERE status_description=? LIMIT 1;";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            pStmt.setString(1, statusName);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                iRes = rSet.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, rSet);
        }
        return iRes;
    }

    @Override
    public Map<Integer, String> getAvailableStatuses() {
        Map<Integer, String> res = new TreeMap<>();
        String SQL = "SELECT id, status_description FROM log.status_lkp ORDER BY id ASC;";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            rSet = pStmt.executeQuery();
            while (rSet.next()) {
                int id = rSet.getInt(1);
                String status = rSet.getString(2);
                res.put(id, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, rSet);
        }
        return res;
    }

    @Override
    public long injectActivity(int module_id, int status_lkp_id, String JSONmsg) {
        long lRes = 0l;
        String SQL = "select activity_id from log.activity_create(?::smallint, ?::smallint, ?::json);";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            pStmt.setInt(1, module_id);
            pStmt.setInt(2, status_lkp_id);
            pStmt.setString(3, JSONmsg);
            rSet = pStmt.executeQuery();
            if (rSet.next()) {
                lRes = rSet.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, rSet);
        }
        return lRes;
    }

    @Override
    public void finalizedActivity(long activity_id, int status_id, String JSONmsg) {
        String SQL = "SELECT * FROM log.activity_commit(?::bigint, ?::smallint, ?::json);";
        Connection dbConnection = null;
        PreparedStatement pStmt = null;
        try {
            dbConnection = dataSource.getConnection();
            pStmt = dbConnection.prepareStatement(SQL);
            pStmt.setLong(1, activity_id);
            pStmt.setInt(2, status_id);
            pStmt.setString(3, JSONmsg);
            pStmt.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DefaultLoggingDBA.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            SQLUtils.release(dbConnection, pStmt, null);
        }
    }
}
