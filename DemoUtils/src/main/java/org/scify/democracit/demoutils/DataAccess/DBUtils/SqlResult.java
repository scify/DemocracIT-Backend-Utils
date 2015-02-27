/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.scify.democracit.demoutils.DataAccess.DBUtils;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author George K.<gkiom@scify.org>
 */
public class SqlResult {
    
    private Statement stmt;
    private ResultSet rSet;

    public SqlResult(Statement stmt, ResultSet rSet) {
        this.stmt = stmt;
        this.rSet = rSet;
    }

    public Statement getStmt() {
        return stmt;
    }

    public ResultSet getRSet() {
        return rSet;
    }
    
    public void release() {
        SQLUtils.release(null, stmt, rSet);
    }
}
