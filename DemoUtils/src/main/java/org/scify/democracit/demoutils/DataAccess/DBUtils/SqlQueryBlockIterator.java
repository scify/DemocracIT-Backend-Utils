/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scify.democracit.demoutils.DataAccess.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author George K.<gkiom@scify.org>
 */
public class SqlQueryBlockIterator implements Iterator<ResultSet> {

    Connection dbConnection;
    String BaseQuery;
    int iMaxCntFetched = 0;
    int iBlockSize = 500;
    int iTotal = -1;
    int iCurPos = 0;
    ResultSet rsCurrentBlock;
    private final String TAG = SqlQueryBlockIterator.class.getName();

    public SqlQueryBlockIterator(Connection dbConnection, String sSQLQuery) {
        // check if trailing ; and remove
        if (sSQLQuery.endsWith(";")) {
            sSQLQuery = sSQLQuery.substring(0, sSQLQuery.indexOf(";"));
        }
        this.BaseQuery = sSQLQuery;
        this.dbConnection = dbConnection;
        // First get count
        String sSQL = "SELECT COUNT(*) FROM (" + sSQLQuery + ") A;";
        // get count result set
        SqlResult res = execQuery(sSQL);
        ResultSet rSet = res.getRSet();
        try {
            if (rSet.next()) {
                iTotal = rSet.getInt(1);
                System.out.format("%s: Found total %d items. Block size: %d %n",
                        getClass().getName(), iTotal, iBlockSize);
            } else {
                throw new RuntimeException("Could not retrieve item count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Could not retrieve item count: " + ex.getMessage());
        } finally {
            // release SQL result
            res.release();
        }
    }

    @Override
    public boolean hasNext() {
        return iCurPos < iTotal;
    }

    @Override
    public ResultSet next() {
        // If we are at the end of the current block
        if (iCurPos % iBlockSize == 0) {
            // get new result set
            SqlResult rsGotBack = fetchBlock(iCurPos / iBlockSize);
            SqlInMemoryResultSet res = null;
            try {
                // Save in-memory
                res = new SqlInMemoryResultSet(rsGotBack.getRSet());
                // Close returned data
                rsGotBack.release();
            } catch (SQLException ex) {
                Logger.getLogger(TAG).log(
                        Level.SEVERE, "Could not ", ex);
                return null;
            }
            rsCurrentBlock = res;
        }
        // increase position
        iCurPos++;
        try {
            // Get next record
            rsCurrentBlock.next();
            // Return current recordset to allow getting record
            return rsCurrentBlock;
        } catch (SQLException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private SqlResult fetchBlock(int i) {
        String sSQL = BaseQuery + " LIMIT " + String.valueOf(i * iBlockSize)
                + ", " + String.valueOf(iBlockSize) + ";";
//        System.err.println("\n" + sSQL + "\n");
        // Exec SQL
        SqlResult hsRes = execQuery(sSQL);
        // return result set
        return hsRes;
    }

    private SqlResult execQuery(String sSQL) {
        PreparedStatement pStmt = null;
        ResultSet rSet = null;
        try {
            pStmt = dbConnection.prepareStatement(sSQL);
            rSet = pStmt.executeQuery();
            SqlResult res = new SqlResult(pStmt, rSet);
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(TAG).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(getClass().getName()
                + " does not support removing items.");
    }
}
